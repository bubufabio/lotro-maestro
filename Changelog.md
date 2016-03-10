## Maestro 2.2.0 ##
_April 15, 2015_

  * Updated the LOTRO instrument audio samples with the new sounds from Update 15.
  * If you open an .abc or .msx file in Maestro, the transcriber "Z" field will be filled with the transcriber info from that file. This lets you preserve the transcriber name if you modify someone else's .abc file in Maestro.
  * Add a new option in Settings > Save & Export > "Remove silence from start of exported ABC". It is enabled by default, which is how Maestro has always worked. You can disable it if you want to export multiple ABC files from a single MIDI file, and you need them to all start at the same time.



---


## Maestro 2.1.1 ##
_December 16, 2014_

  * Revert LOTRO instrument samples since they were rolled back in the game.


---


## Maestro 2.1.0 ##
_December 15, 2014_

#### New Features ####
  * Updated the LOTRO instrument audio samples with the new tuned instrument sounds from the game.
  * Updated the Maestro configuration for Horn, Clarinet, and Pibgorn so it will now output notes in the full 3-octave range, now that they no longer have breath sounds.

#### Bug Fixes ####
  * Fixed a bug where you could not edit or export a song that was loaded from an ABC file that didn't have a "C:" or "Z:" field.
  * Editing a song from an ABC file now correctly loads the time signature rather than defaulting to 4/4.


---


## Maestro 2.0.0 ##
_September 14, 2014_

#### New Features ####
  * Maestro now works with songs that change tempo! I've overhauled the MIDI-to-ABC conversion logic, so the exported ABC songs play at their original tempo and don't stutter after the tempo changes.
    * When a song contains tempo changes, you'll see an extra row in the track list that shows you how the tempo changes over the course of the song.
    * Because LOTRO doesn't understand the ABC notation for tempo changes mid-song, all note lengths are multiplied by the ratio of the actual tempo and the main tempo. For example, if the tempo changes from 100 BPM to 111 BPM, all note lengths are multiplied by 111/100 for that part of the song.
    * When the tempo changes in a song, the new tempo will be written in a comment, for example: `%%Q: 111`
    * The tempo-change comments are used by Maestro and ABC Player to understand the tempo changes within a song. This lets you open an ABC file with Maestro, and it will still understand the tempo changes.

  * You can now save Maestro Song files, so you don't have to start from scratch if you want to export a song again with some different options.
    * ![http://lotro-maestro.googlecode.com/svn/wiki/images/msxfile_32.png](http://lotro-maestro.googlecode.com/svn/wiki/images/msxfile_32.png)
    * Files are saved in XML format, with the extension .msx (for Maestro Song XML). If you use the installer, this extension will be registered to open with Maestro by default.
    * The .msx files do NOT store the original MIDI file that you opened, so you need to keep the MIDI file around as well.

  * Bar lines are now shown in the track list, and there's a bar counter in the lower-right, just like there is in ABC Player.

  * The lengths of notes in Maestro's ABC output is normalized, so you won't get notes that are a weird length, like an eleven-eighths note. Instead, each note is made up of tied notes of proper lengths: whole, half, quarter, eighth, sixteenth note etc., or a dotted quarter note, dotted eighth note, etc. (However, due to tempo changes, you may still see a note with an ugly fraction like 111/100, but they will still be proper note lengths like a quarter note).

#### Bug Fixes ####
  * Numerous small bug fixes.


---


## Maestro 1.4.1 ##
_March 16, 2014_

#### Bug Fixes ####
  * Fixed a bug with the volume bar showing incorrect values when switching between ABC parts.


---


## Maestro 1.4.0 ##
_March 14, 2014_

#### New Features ####
  * You can now modify the output volume (+pp+, +mf+, etc.) of each track in the ABC file. When a track is selected, there is a volume bar beneath the octave transpose.
  * While you're adjusting the volume, the display shows the volume of each note, instead of the pitch like it normally does.
  * If you adjust the volume past the minimum or maximum volume, the note will be drawn in red and the volume will be clipped to +pppp+ or +ffff+.

#### Bug Fixes ####
  * Fixed a crash when opening certain midi files that have extreme pitch bends.
  * ABC Player and Maestro now have the same version number.


---


## Maestro 1.0.0 ##
_August 31, 2013_

  * Initial Release


---


## ABC Player 1.3.1 ##
_August 31, 2013_

#### New Features ####
  * Added an option to show the full name of each part in the track list, without trimming off the song name from the start.
  * The volume now goes to eleven! The maximum volume is a bit louder than it used to be.


---


## ABC Player 1.3.0 ##
_July 20, 2013_

### New Features ###
  * Added support for the Pibgorn instrument. There are several notes that play at the wrong pitch to match the broken behavior of the Pibgorn in the game.
  * Added a Solo button `[S]` to let you easily listen to a single part solo without having to mute the rest of the tracks.
  * The instrument dropdown is now sorted by name.

#### Bug Fixes ####
  * The +pppp+ and +ffff+ dynamics specifiers should no longer cause errors.


---


## ABC Player 1.2.2 ##
_January 6, 2012_

#### Bug Fixes ####
  * ABC Player now works with Java 6 Update 30+.
  * ABC Player's play speed should match LOTRO's play speed if a song has both the M: and L: header fields specified.
  * The song position bar now lets you drag past 35:47 on long songs.
  * Misc tweaks to the UI.


---


## ABC Player 1.2.0 ##
_March 15, 2011_

#### New Features ####
  * You can now export songs directly to MP3! Requires the (free) LAME mp3 converter.
  * The volume slider is back. If you're using Windows Vista or Windows 7, it will use the system's per-application volume (kept in sync with the volume set for ABC Player in the system volume mixer).
  * Changed the stereo effect to be less dependent on the order of the parts in the file, and more on the instrument used for the part. For example, flute will always be panned to the left. If the same instrument is used in multiple parts, the second part will be be panned to opposite speaker, and the third part will be panned to the center.
  * If a part name includes the word "Left", it will always be panned to the left speaker. Likewise for the words "Right, and "Center" or "Middle".

#### Bug Fixes ####
  * When a long note is held on a woodwind instrument while stringed instruments are playing many fast notes, the note on the woodwind should no longer be cut off before it actually ends.
  * If the same note is repeated quickly on a woodwind instrument, the difference between notes is sharper, and sounds more like it does in LOTRO.
  * Staccato (short) notes on woodwind instruments are more... staccato, to sound more like they do in LOTRO.
  * Bagpipe drone notes (C, through B,) now sustain forever rather than fading out after ~8 seconds, to match how they sound in LOTRO.


---


## ABC Player 1.1.0 ##
_August 18, 2010_

#### New Features ####
  * Now in Stereo! In songs with multiple parts, the second and subsequent parts are panned slightly to the right or left to emulate how it would sound playing with other people in the game. (The stereo effect in ABC Player is slightly less than in the game).
  * You can now paste songs from the clipboard to open them. Use Ctrl+V to create a new song using the text from the clipboard. Use Ctrl+Shift+V to add the text from the clipboard to the current song (useful if pasting a multi-part song from thefatlute.com, for example).
  * You can also use Ctrl+V and Ctrl+Shift+V with actual .abc files, as an alternative to dragging and dropping the files.
  * You can now append additional parts to a song once it's already open, including the same song multiple times. You can accomplish this in a variety of ways:
  * Choose "Append ABC file(s)..." from the File menu (Ctrl+Shift+O).
  * Choose "Append from clipboard" from the File menu (Ctrl+Shift+V) if the file is on the clipboard.
  * Hold down Ctrl and drag/drop the file onto ABC Player.
  * Added a tempo slider to adjust the tempo of playback. Right-click on the slider to quickly toggle between 0% and 100%. Also removed the volume slider, as it didn't seem very useful.
  * ABC Player will now warn you about ABC errors that are specific to Lord of the Rings Online. These include notes that are too low, too high, too short, too long; chords with too many notes; or volume +dynamics+ inside chords. You can choose to disable these warnings if you want.
  * Added support for the following key signature modes in addition to major and minor: Dorian, Phrygian, Lydian, Mixolydian, Aeolian, Ionian, and Locrian.

#### Bug Fixes ####
  * ABC Player now handles notes with incorrect octave markers (like c,, or A') the same way that LOTRO does, rather than giving an error. The comma always lowers the octave and the apostrophe always raises the octave, regardless of whether the note letter is upper or lower case.
  * Tuplets that contain chords should now have the correct note length.
  * Fixed an issue when determining the speed to play songs that have a meter with a denominator that's not 4 (e.g. 6/8). Hopefully all songs should play at the correct speed now.
  * Reduced the volume difference between +ppp+ and +fff+. ABC Player's volume dynamics should now more closely match LOTRO's.
  * Fixed the harp's ^a and b notes so they don't sound like clicks.
  * Occasionally when changing instruments, you'd hear the instruments playing at full volume for a few seconds. This should no longer happen.
  * Removed the power of two restriction on the meter's denominator.
  * You can now change the instrument and see bar numbers even if the abc file is missing the (required) "X:" line at the beginning. Note that the "X:" line is still required at the beginning of each part in multi-part songs, even if the parts are in separate files.
  * ABC Player now ignores slurs () without generating errors, to mimic LOTRO's behavior.
  * Playing a part that has note ties on the cowbell should no longer generate erroneous note tie errors.


---


## ABC Player 1.0.0 ##
_July 14, 2010_

  * Initial release
  * This hasn't been tested by anyone other than me, so it may fail on certain ABC files. Let me know if you find an ABC file that plays in LOTRO but not in ABC Player.