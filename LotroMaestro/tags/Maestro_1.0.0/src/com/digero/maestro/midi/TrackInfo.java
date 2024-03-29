package com.digero.maestro.midi;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import com.digero.common.abc.LotroInstrument;
import com.digero.common.midi.IMidiConstants;
import com.digero.common.midi.KeySignature;
import com.digero.common.midi.MidiConstants;
import com.digero.common.midi.Note;
import com.digero.common.midi.TimeSignature;
import com.digero.maestro.abc.TimingInfo;
import com.sun.media.sound.MidiUtils;

public class TrackInfo implements IMidiConstants {
	private SequenceInfo sequenceInfo;

	private int trackNumber;
	private String name;
	private TimeSignature timeSignature = null;
	private KeySignature keySignature = null;
	private Set<Integer> instruments;
	private List<NoteEvent> noteEvents;
	private SortedSet<Integer> notesInUse;
	private boolean isDrumTrack;

	@SuppressWarnings("unchecked")
	TrackInfo(SequenceInfo parent, Track track, int trackNumber, MidiUtils.TempoCache tempoCache,
			SequenceDataCache sequenceCache) throws InvalidMidiDataException {
		this.sequenceInfo = parent;
		this.trackNumber = trackNumber;

		Sequence song = sequenceInfo.getSequence();

		instruments = new HashSet<Integer>();
		noteEvents = new ArrayList<NoteEvent>();
		notesInUse = new TreeSet<Integer>();
		List<NoteEvent>[] notesOn = new List[16];
		int notesNotTurnedOff = 0;

		int[] pitchBend = new int[16];
		for (int j = 0, sz = track.size(); j < sz; j++) {
			MidiEvent evt = track.get(j);
			MidiMessage msg = evt.getMessage();

			if (msg instanceof ShortMessage) {
				ShortMessage m = (ShortMessage) msg;
				int cmd = m.getCommand();
				int c = m.getChannel();

				if (noteEvents.isEmpty())
					isDrumTrack = (c == DRUM_CHANNEL);
				else if (isDrumTrack != (c == DRUM_CHANNEL))
					System.err.println("Track contains both notes and drums");

				if (notesOn[c] == null)
					notesOn[c] = new ArrayList<NoteEvent>();

				if (cmd == ShortMessage.NOTE_ON || cmd == ShortMessage.NOTE_OFF) {
					int noteId = m.getData1() + (isDrumTrack ? 0 : pitchBend[c]);
					int velocity = m.getData2() * sequenceCache.getVolume(c, evt.getTick()) / DEFAULT_CHANNEL_VOLUME;
					if (velocity > 127)
						velocity = 127;
					long micros = MidiUtils.tick2microsecond(song, evt.getTick(), tempoCache);

					if (cmd == ShortMessage.NOTE_ON && velocity > 0) {
						Note note = Note.fromId(noteId);
						if (note == null) {
//							throw new InvalidMidiDataException("Encountered unrecognized note ID: " + noteId);
							continue; // Note was probably bent out of range. Not great, but not a reason to fail.
						}

						NoteEvent ne = new NoteEvent(note, velocity, micros, micros);

						Iterator<NoteEvent> onIter = notesOn[c].iterator();
						while (onIter.hasNext()) {
							NoteEvent on = onIter.next();
							if (on.note.id == ne.note.id) {
								onIter.remove();
								noteEvents.remove(on);
								notesNotTurnedOff++;
								break;
							}
						}

						if (!isDrumTrack) {
							instruments.add(sequenceCache.getInstrument(c, evt.getTick()));
						}
						noteEvents.add(ne);
						notesInUse.add(ne.note.id);
						notesOn[c].add(ne);
					}
					else {
						Iterator<NoteEvent> iter = notesOn[c].iterator();
						while (iter.hasNext()) {
							NoteEvent ne = iter.next();
							if (ne.note.id == noteId) {
								iter.remove();
								ne.endMicros = micros;
								break;
							}
						}
					}
				}
				else if (cmd == ShortMessage.PITCH_BEND && !isDrumTrack) {
					long micros = MidiUtils.tick2microsecond(song, evt.getTick(), tempoCache);

					double pct = 2 * (((m.getData1() | (m.getData2() << 7)) / (double) (1 << 14)) - 0.5);
					int bend = (int) Math.round(pct * sequenceCache.getPitchBendRange(m.getChannel(), evt.getTick()));

					if (bend != pitchBend[c]) {
						List<NoteEvent> bentNotes = new ArrayList<NoteEvent>();
						for (NoteEvent ne : notesOn[c]) {
							ne.endMicros = micros;
							// If the note is too short, just skip it
							if (ne.getLength() < TimingInfo.SHORTEST_NOTE_MICROS) {
								noteEvents.remove(ne);
								micros = ne.startMicros;
							}

							Note bn = Note.fromId(ne.note.id + bend - pitchBend[c]);
							// If bn is null , the note was bent out of the 0-127 range. 
							// Not much we can do except skip it.
							if (bn != null) {
								NoteEvent bne = new NoteEvent(bn, ne.velocity, micros, micros);
								noteEvents.add(bne);
								bentNotes.add(bne);
							}
						}
						notesOn[c] = bentNotes;
						pitchBend[c] = bend;
					}
				}
			}
			else if (msg instanceof MetaMessage) {
				MetaMessage m = (MetaMessage) msg;
				int type = m.getType();

				if (type == META_TRACK_NAME && name == null) {
					try {
						byte[] data = m.getData();
						String tmp = new String(data, 0, data.length, "US-ASCII").trim();
						if (tmp.length() > 0 && !tmp.equalsIgnoreCase("untitled")
								&& !tmp.equalsIgnoreCase("WinJammer Demo"))
							name = tmp;
					}
					catch (UnsupportedEncodingException ex) {
						// Ignore.  This should never happen...
					}
				}
				else if (type == META_KEY_SIGNATURE && keySignature == null) {
					keySignature = new KeySignature(m);
				}
				else if (type == META_TIME_SIGNATURE && timeSignature == null) {
					timeSignature = new TimeSignature(m);
				}
			}
		}

		// Turn off notes that are on at the end of the song.  This shouldn't happen...
		int ctNotesOn = 0;
		for (List<NoteEvent> notesOnChannel : notesOn) {
			if (notesOnChannel != null)
				ctNotesOn += notesOnChannel.size();
		}
		if (ctNotesOn > 0) {
			System.err.println((ctNotesOn + notesNotTurnedOff) + " note(s) not turned off at the end of the track.");

			for (List<NoteEvent> notesOnChannel : notesOn) {
				if (notesOnChannel != null)
					noteEvents.removeAll(notesOnChannel);
			}
		}

		noteEvents = Collections.unmodifiableList(noteEvents);
		notesInUse = Collections.unmodifiableSortedSet(notesInUse);
		instruments = Collections.unmodifiableSet(instruments);
	}

	public TrackInfo(SequenceInfo parent, int trackNumber, String name, LotroInstrument instrument,
			TimeSignature timeSignature, KeySignature keySignature, List<NoteEvent> noteEvents) {
		this.sequenceInfo = parent;
		this.trackNumber = trackNumber;
		this.name = name;
		this.timeSignature = timeSignature;
		this.keySignature = keySignature;
		this.instruments = new HashSet<Integer>();
		this.instruments.add(instrument.midiProgramId);
		this.noteEvents = noteEvents;
		this.notesInUse = new TreeSet<Integer>();
		for (NoteEvent ne : noteEvents) {
			this.notesInUse.add(ne.note.id);
		}
		this.isDrumTrack = false;

		this.noteEvents = Collections.unmodifiableList(this.noteEvents);
		this.notesInUse = Collections.unmodifiableSortedSet(this.notesInUse);
		this.instruments = Collections.unmodifiableSet(this.instruments);
	}

	public SequenceInfo getSequenceInfo() {
		return sequenceInfo;
	}

	public int getTrackNumber() {
		return trackNumber;
	}

	public boolean hasName() {
		return name != null;
	}

	public String getName() {
		if (name == null)
			return "Track " + trackNumber;
		return name;
	}

	public KeySignature getKeySignature() {
		return keySignature;
	}

	public TimeSignature getTimeSignature() {
		return timeSignature;
	}

	@Override
	public String toString() {
		return getName();
	}

	public boolean isDrumTrack() {
		return isDrumTrack;
	}

	/** Gets an unmodifiable list of the note events in this track. */
	public List<NoteEvent> getEvents() {
		return noteEvents;
	}

	public boolean hasEvents() {
		return !noteEvents.isEmpty();
	}

	public SortedSet<Integer> getNotesInUse() {
		return notesInUse;
	}

	public int getEventCount() {
		return noteEvents.size();
	}

	public String getEventCountString() {
		if (getEventCount() == 1) {
			return "1 note";
		}
		return getEventCount() + " notes";
	}

	public String getInstrumentNames() {
		if (isDrumTrack)
			return "Drums";

		if (instruments.size() == 0) {
			if (hasEvents())
				return MidiConstants.getInstrumentName(0);
			else
				return "<None>";
		}

		String names = "";
		boolean first = true;
		for (int i : instruments) {
			if (!first)
				names += ", ";
			else
				first = false;

			names += MidiConstants.getInstrumentName(i);
		}

		return names;
	}

	public int getInstrumentCount() {
		return instruments.size();
	}

	public Set<Integer> getInstruments() {
		return instruments;
	}

//	public int[] addNoteVelocities(int[] velocities) {
//		if (velocities == null)
//			velocities = new int[this.noteVelocities.length];
//		for (int i = 0; i < this.noteVelocities.length; i++) {
//			velocities[i] += this.noteVelocities[i];
//		}
//		return velocities;
//	}
}