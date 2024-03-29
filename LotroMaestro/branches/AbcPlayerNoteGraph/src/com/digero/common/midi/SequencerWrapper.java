package com.digero.common.midi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;
import javax.swing.Timer;

import com.sun.media.sound.MidiUtils;
import com.sun.media.sound.MidiUtils.TempoCache;

public class SequencerWrapper implements IMidiConstants {
	public static final int UPDATE_FREQUENCY_MILLIS = 50;
	public static final long UPDATE_FREQUENCY_MICROS = UPDATE_FREQUENCY_MILLIS * 1000;

	protected Sequencer sequencer;
	protected Receiver receiver;
	protected Transmitter transmitter;
	private long dragPosition;
	private boolean isDragging;
	private TempoCache tempoCache = new TempoCache();
	private boolean customSequencer = false;

	private Timer updateTimer;

	private List<SequencerListener> listeners = null;

	public SequencerWrapper() throws MidiUnavailableException {
		sequencer = MidiSystem.getSequencer(false);
		sequencer.open();
		transmitter = sequencer.getTransmitter();
		receiver = MidiSystem.getReceiver();

		connectTransmitter();

		updateTimer = new Timer(UPDATE_FREQUENCY_MILLIS, timerTick);
		updateTimer.start();
	}

	protected void connectTransmitter() {
		transmitter.setReceiver(receiver);
	}

	public SequencerWrapper(Sequencer sequencer, Transmitter transmitter, Receiver receiver) {
		customSequencer = true;
		this.sequencer = sequencer;
		this.transmitter = transmitter;
		this.receiver = receiver;

		if (sequencer.getSequence() != null) {
			tempoCache.refresh(sequencer.getSequence());
		}

		updateTimer = new Timer(50, timerTick);
		updateTimer.start();
	}

	private TimerActionListener timerTick = new TimerActionListener();
	private long lastUpdatePosition = -1;

	private class TimerActionListener implements ActionListener {
		private boolean lastRunning = false;

		public void actionPerformed(ActionEvent e) {
			if (sequencer != null) {
				long songPos = sequencer.getMicrosecondPosition();
				boolean running = sequencer.isRunning();
				if (songPos >= getLength()) {
					// There's a bug in Sun's RealTimeSequencer, where there is a possible 
					// deadlock when calling setMicrosecondPosition(0) exactly when the sequencer 
					// hits the end of the sequence.  It looks like it's safe to call 
					// sequencer.setTickPosition(0).
					sequencer.stop();
					sequencer.setTickPosition(0);
					lastUpdatePosition = songPos;
				}
				else if (lastUpdatePosition != songPos) {
					lastUpdatePosition = songPos;
					fireChangeEvent(SequencerProperty.POSITION);
				}
				if (lastRunning != running) {
					lastRunning = running;
					fireChangeEvent(SequencerProperty.IS_RUNNING);
				}
			}
		}
	};

	public void reset(boolean fullReset) {
		stop();
		setPosition(0);

		boolean oldReset = customSequencer || !fullReset;

		if (!oldReset) {
			Sequence seqSave = sequencer.getSequence();
			try {
				sequencer.setSequence((Sequence) null);
			}
			catch (InvalidMidiDataException e) {
				// This won't happen
				throw new RuntimeException(e);
			}

			sequencer.close();
			transmitter.close();
			receiver.close();

			try {
				sequencer = MidiSystem.getSequencer(false);
				sequencer.open();
				transmitter = sequencer.getTransmitter();
				receiver = MidiSystem.getReceiver();
			}
			catch (MidiUnavailableException e1) {
				throw new RuntimeException(e1);
			}

			try {
				sequencer.setSequence(seqSave);
			}
			catch (InvalidMidiDataException e) {
				// This won't happen
				throw new RuntimeException(e);
			}

			connectTransmitter();

			try {
				ShortMessage msg = new ShortMessage();
				msg.setMessage(ShortMessage.SYSTEM_RESET);
				receiver.send(msg, -1);

//				for (int i = 0; i < CHANNEL_COUNT; i++) {
//					msg.setMessage(ShortMessage.PROGRAM_CHANGE, i, 0, 0);
//					receiver.send(msg, -1);
//					msg.setMessage(ShortMessage.CONTROL_CHANGE, i, ALL_CONTROLLERS_OFF, 0);
//					receiver.send(msg, -1);
//					msg.setMessage(ShortMessage.CONTROL_CHANGE, i, REGISTERED_PARAMETER_NUMBER_COARSE,
//							REGISTERED_PARAM_PITCH_BEND_RANGE);
//					receiver.send(msg, -1);
//					msg.setMessage(ShortMessage.CONTROL_CHANGE, i, DATA_ENTRY_COARSE, 12);
//					receiver.send(msg, -1);
//				}
			}
			catch (InvalidMidiDataException e) {
				e.printStackTrace();
			}
		}

		if (oldReset) {
			// Reset the instruments
			boolean isOpen = sequencer.isOpen();
			try {
				if (!isOpen)
					sequencer.open();

				ShortMessage msg = new ShortMessage();
				msg.setMessage(ShortMessage.SYSTEM_RESET);
				receiver.send(msg, -1);
				for (int i = 0; i < CHANNEL_COUNT; i++) {
					msg.setMessage(ShortMessage.PROGRAM_CHANGE, i, 0, 0);
					receiver.send(msg, -1);
					msg.setMessage(ShortMessage.CONTROL_CHANGE, i, ALL_CONTROLLERS_OFF, 0);
					receiver.send(msg, -1);
//					msg.setMessage(ShortMessage.CONTROL_CHANGE, i, REGISTERED_PARAMETER_NUMBER_COARSE,
//							REGISTERED_PARAM_PITCH_BEND_RANGE);
//					receiver.send(msg, -1);
//					msg.setMessage(ShortMessage.CONTROL_CHANGE, i, DATA_ENTRY_COARSE, 12);
//					receiver.send(msg, -1);
				}
			}
			catch (MidiUnavailableException e) {
				// Ignore
			}
			catch (InvalidMidiDataException e) {
				// Ignore
				e.printStackTrace();
			}

			if (!isOpen)
				sequencer.close();
		}
	}

	public long getTickPosition() {
		return sequencer.getTickPosition();
	}

	public void setTickPosition(long tick) {
		if (tick != getTickPosition()) {
			sequencer.setTickPosition(tick);
			lastUpdatePosition = sequencer.getMicrosecondPosition();
			fireChangeEvent(SequencerProperty.POSITION);
		}
	}

	public long getPosition() {
		return sequencer.getMicrosecondPosition();
	}

	public void setPosition(long position) {
		if (position != getPosition()) {
			if (position == 0)
				sequencer.setTickPosition(0);
			else
				sequencer.setMicrosecondPosition(position);
			lastUpdatePosition = sequencer.getMicrosecondPosition();
			fireChangeEvent(SequencerProperty.POSITION);
		}
	}

	public long getLength() {
		return sequencer.getMicrosecondLength();
	}

	public float getTempoFactor() {
		return sequencer.getTempoFactor();
	}

	public void setTempoFactor(float tempo) {
		if (tempo != getTempoFactor()) {
			sequencer.setTempoFactor(tempo);
			fireChangeEvent(SequencerProperty.TEMPO);
		}
	}

	public boolean isRunning() {
		return sequencer.isRunning();
	}

	public void setRunning(boolean isRunning) {
		if (isRunning != this.isRunning()) {
			if (isRunning)
				sequencer.start();
			else
				sequencer.stop();
			timerTick.lastRunning = isRunning;
			fireChangeEvent(SequencerProperty.IS_RUNNING);
		}
	}

	public void start() {
		setRunning(true);
	}

	public void stop() {
		setRunning(false);
	}

	public boolean getTrackMute(int track) {
		return sequencer.getTrackMute(track);
	}

	public void setTrackMute(int track, boolean mute) {
		if (mute != this.getTrackMute(track)) {
			sequencer.setTrackMute(track, mute);
			fireChangeEvent(SequencerProperty.TRACK_ACTIVE);
		}
	}

	public boolean getTrackSolo(int track) {
		return sequencer.getTrackSolo(track);
	}

	public void setTrackSolo(int track, boolean solo) {
		if (solo != this.getTrackSolo(track)) {
			sequencer.setTrackSolo(track, solo);
			fireChangeEvent(SequencerProperty.TRACK_ACTIVE);
		}
	}

	/**
	 * Takes into account both muting and solo.
	 */
	public boolean isTrackActive(int track) {
		Sequence song = sequencer.getSequence();

		if (song == null)
			return true;

		if (sequencer.getTrackSolo(track))
			return true;

		for (int i = song.getTracks().length - 1; i >= 0; --i) {
			if (i != track && sequencer.getTrackSolo(i))
				return false;
		}

		return !sequencer.getTrackMute(track);
	}

	/**
	 * Overriden by NoteFilterSequencerWrapper. On SequencerWrapper for
	 * convienience.
	 */
	public boolean isNoteActive(int noteId) {
		return true;
	}

	/**
	 * If dragging, returns the drag position. Otherwise returns the song
	 * position.
	 */
	public long getThumbPosition() {
		return isDragging() ? getDragPosition() : getPosition();
	}

	/** If dragging, returns the drag tick. Otherwise returns the song tick. */
	public long getThumbTick() {
		return isDragging() ? getDragTick() : getTickPosition();
	}

	public long getDragPosition() {
		return dragPosition;
	}

	public long getDragTick() {
		return MidiUtils.microsecond2tick(getSequence(), getDragPosition(), tempoCache);
	}

	public void setDragTick(long tick) {
		setDragPosition(MidiUtils.tick2microsecond(getSequence(), tick, tempoCache));
	}

	public void setDragPosition(long dragPosition) {
		if (this.dragPosition != dragPosition) {
			this.dragPosition = dragPosition;
			fireChangeEvent(SequencerProperty.DRAG_POSITION);
		}
	}

	public boolean isDragging() {
		return isDragging;
	}

	public void setDragging(boolean isDragging) {
		if (this.isDragging != isDragging) {
			this.isDragging = isDragging;
			fireChangeEvent(SequencerProperty.IS_DRAGGING);
		}
	}

	public void addChangeListener(SequencerListener l) {
		if (listeners == null)
			listeners = new ArrayList<SequencerListener>();

		listeners.add(l);
	}

	public void removeChangeListener(SequencerListener l) {
		if (listeners != null)
			listeners.remove(l);
	}

	protected void fireChangeEvent(SequencerProperty property) {
		if (listeners != null) {
			SequencerEvent e = new SequencerEvent(this, property);
			for (SequencerListener l : listeners) {
				l.propertyChanged(e);
			}
		}
	}

	public void setSequence(Sequence sequence) throws InvalidMidiDataException {
		if (sequencer.getSequence() != sequence) {
			boolean preLoaded = isLoaded();
			sequencer.setSequence(sequence);
			if (sequence != null)
				tempoCache.refresh(sequence);
			if (preLoaded != isLoaded())
				fireChangeEvent(SequencerProperty.IS_LOADED);
			fireChangeEvent(SequencerProperty.LENGTH);
			fireChangeEvent(SequencerProperty.SEQUENCE);
		}
	}

	public void clearSequence() {
		try {
			setSequence(null);
		}
		catch (InvalidMidiDataException e) {
			// This shouldn't happen
			throw new RuntimeException(e);
		}
	}

	public boolean isLoaded() {
		return sequencer.getSequence() != null;
	}

	public Sequence getSequence() {
		return sequencer.getSequence();
	}

	public Transmitter getTransmitter() {
		return transmitter;
	}

	public Receiver getReceiver() {
		return receiver;
	}

	public void open() throws MidiUnavailableException {
		sequencer.open();
	}

	public void close() {
		sequencer.close();
	}
}
