<table cellpadding='0' width='800'><tr><td valign='bottom'>
<font size='7'>Maestro</font>
</td><td width='48px'><img src='http://lotro-maestro.googlecode.com/svn/wiki/images/maestro_48.png' /></td></tr></table>

Maestro is a MIDI to ABC converter for The Lord of the Rings Online. Use it to arrange multi-part ABC files to play in the game with your friends, or create solo pieces to play yourself.

# Features #
  * Convert MIDI files into solo or multi-part ABC files for playing with your friends in game.
  * Listen to a live ABC Player preview of the song, which automatically updates as you choose tracks and arrange the song.
  * See all of the <font color='#045CFF'><b>notes</b></font> in the MIDI file, with notes that are out of range of the LOTRO instruments highlighted in <font color='#FF4D00'><b>red</b></font>.
  * Choose how to map drum notes between MIDI drums and ABC drums.
  * Open existing ABC files to change the arrangement, transpose tracks, change drums, etc.

## Tips and hints ##
  * To play a track solo (mute other tracks), hold down the right mouse button on the track. This also works for individual drum notes when editing drums.
  * If the rhythm sounds wrong in the ABC Preview, try the **Triplets/swing rhythm** checkbox.
  * You can right click on an .abc or .mid file and choose "Edit with Maestro" to open Maestro directly.

# Walkthrough #
![http://lotro-maestro.googlecode.com/svn/wiki/images/maestro_v2.0.0.png](http://lotro-maestro.googlecode.com/svn/wiki/images/maestro_v2.0.0.png)

Starting from the top left of the screenshot:

<table cellpadding='8' width='800'><tr><td width='180px' valign='top'><img src='http://lotro-maestro.googlecode.com/svn/wiki/images/walkthrough/song_info.png' /></td><td valign='top'>
<b>Song Info</b>

<ul><li><b>T:</b> The title of the song<br>
</li><li><b>C:</b> The composer of the song<br>
</li><li><b>Z:</b> The ABC transcriber, this should be your name (Maestro will remember your name and add it to all of the ABC files you export)<br>
</td></tr></table>
<table cellpadding='8' width='800'><tr><td width='180px' valign='top'><img src='http://lotro-maestro.googlecode.com/svn/wiki/images/walkthrough/song_parts.png' /></td><td valign='top'>
<b>Song Parts</b></li></ul>

<ul><li>The list of parts you've added to the song. Each part will be played by a different person in the game. Select a part in this list to choose its instrument on the right, and pick which MIDI tracks are in the part.<br>
</td></tr></table></li></ul>


<table cellpadding='8' width='800'><tr><td width='180px' valign='top'><img src='http://lotro-maestro.googlecode.com/svn/wiki/images/walkthrough/export_settings.png' /></td><td valign='top'>
<b>Export Settings</b> - These settings affect all parts of the ABC song when it exported.<br>
<br>
<ul><li><b>Transpose</b> - Shift all parts up or down by semitones (12 semitones = 1 octave). Use this for minor adjustments to get the notes to fit within LOTRO's 3-octave range.<br>
</li><li><b>Tempo</b> - Speed up or slow down the ABC song. If the song contains tempo changes, then this only displays the Main Tempo of the song; that is, the tempo that's used by the majority of the song. Changing this value will adjust the tempo for the entire song proportionally.<br>
</li><li><b>Meter</b> - Adjust the key signature of the ABC file. This only affects the ABC text in the exported file, but doesn't affect what it sounds like when played in the game.<br>
</li><li><b>Triplets/swing rhythm</b> - Tweak the timing to allow for triplets or a swing rhythm. This can cause short/fast notes to incorrectly be detected as triplets. Leave it unchecked unless the song has triplets or a swing rhythm.<br>
</td></tr></table></li></ul>


<table cellpadding='8' width='800'><tr><td width='180px' valign='top'><img src='http://lotro-maestro.googlecode.com/svn/wiki/images/walkthrough/part_settings.png' /></td><td valign='top'>
<b>Part Settings</b>

<ul><li><b>X:</b> The part number of the given part. The part number will be automatically assigned based on the instrument. Use the gear icon to change numbering settings. See <a href='Maestro#Advanced_options.md'>Advanced options</a> below for more info.<br>
</li><li><b>I:</b> The instrument played by this part.<br>
</li><li><b>Part name:</b> The name of this individual part. By default, this is just the name of the part's instrument. This part name will be combined with the song name when exporting the song. See <a href='Maestro#Advanced_options.md'>Advanced options</a> below for more info.<br>
</td></tr></table></li></ul>


<table cellpadding='8' width='800'><tr><td width='180px' valign='top'><img src='http://lotro-maestro.googlecode.com/svn/wiki/images/walkthrough/track_list.png' /></td><td valign='top'>
<b>Track List</b>

This is the list of tracks that were in the MIDI or ABC file that you opened.<br>
<ul><li><b>Track name</b> - Check the checkbox next to the track name to include it in the tracks played for the current part.<br>
</li><li><b>Track transpose</b> - Transpose individual tracks up or down by full octaves to get the track into the playable range of the selected instrument.<br>
</li><li><b>Track volume</b> - Adjust the volume of this track in the output ABC file. While adjusting the volume, the note display shows the volume of each note in the track. Any notes adjusted quieter than +pppp+ or louder than +ffff+ are drawn in <font color='#FF4D00'><b>red</b></font> to indicate that their volume was clipped.<br>
</li><li><b>Notes</b> - This is a graphical display of the notes in the track.<br>
<ul><li><font color='#4081FF'><b>Blue</b></font> notes can be played by the selected instrument.<br>
</li><li><font color='#FF4D00'><b>Red</b></font> notes are out of the playable range of the instrument and will be transposed up or down by one or more octaves to get it into the playable range.<br>
</li><li><font color='darkgray'><b>Gray</b></font> notes when in ABC Preview mode indicate that the track isn't selected in any ABC part.<br>
</li><li>Click to skip to that position in the song.<br>
</li><li>Hold down the right mouse button on a track to temporarily mute all other tracks (play the track solo).<br>
</td></tr></table></li></ul></li></ul>


<table cellpadding='8' width='800'><tr><td width='180px' valign='top'><img src='http://lotro-maestro.googlecode.com/svn/wiki/images/walkthrough/play_controls.png' /></td><td valign='top'>
<b>Play controls</b>

<ul><li><b>ABC Preview</b> - Play the song as it would sound if it were played in LOTRO. This is like a built-in ABC Player that automatically updates as you add and remove tracks, switch instruments, etc.<br>
</li><li><b>Play/Pause/Stop, Volume</b> - Self explanatory :)<br>
</td></tr></table></li></ul>

## Drums ##
![http://lotro-maestro.googlecode.com/svn/wiki/images/maestro_drums_v2.0.0.png](http://lotro-maestro.googlecode.com/svn/wiki/images/maestro_drums_v2.0.0.png)

When you choose Drums as the instrument, any track you select will be expanded to show each individual drum type. From there, you can choose which LOTRO drum note it should use.

You can load and save drum mappings to a file. Click the Import or Export buttons just below the track name.

Hold down the right mouse button on an individual drum note to play that note solo, to hear what that drum sounds like. When in ABC Preview mode, this will play the ABC drum note.

## Advanced options ##
You can configure advanced options from the the Tools menu > Options.

![http://lotro-maestro.googlecode.com/svn/wiki/images/walkthrough/options_partnumbering.png](http://lotro-maestro.googlecode.com/svn/wiki/images/walkthrough/options_partnumbering.png)
![http://lotro-maestro.googlecode.com/svn/wiki/images/walkthrough/options_partnaming.png](http://lotro-maestro.googlecode.com/svn/wiki/images/walkthrough/options_partnaming.png)
![http://lotro-maestro.googlecode.com/svn/wiki/images/walkthrough/options_exportsave.png](http://lotro-maestro.googlecode.com/svn/wiki/images/walkthrough/options_exportsave.png)

### ABC Part Numbering ###
In order to keep your ABC part numbers predictable and consistent between songs, part numbers for ABC parts are automatically assigned.
  * **First part number** - The default part number for the given instrument.
  * **Increment** - If you have multiple parts that use the same instrument, this increment will be used to set the part number of the given part.
  * More than one instrument can have the same first part number. In that case, they're treated as if they were the same instrument for the purposes of numbering parts.

### ABC Part Naming ###
This dialog lets you configure the full name of each part when you export it to ABC (the **T:** field in the exported ABC file). The full name is what's displayed in LOTRO when you type `/play <song> [part number]`.

The variables you can use are:
  * **`$FileName`** - The name of the ABC file, without the .abc extension.
  * **`$FilePath`** - The path to the ABC file including the ABC file name, if it is in a subdirectory of the LOTRO/Music directory. If the file is saved directly in the LOTRO/Music directory, this will be the same as **`$FileName`**.
  * **`$PartInstrument`** - The instrument for the individual ABC part.
  * **`$PartName`** - The part name for the individual ABC part.
  * **`$PartNumber`** - The part number for the individual ABC part.
  * **`$SongComposer`** - The song composer's name, as entered in the **C:** field.
  * **`$SongLength`** - The playing time of the song in mm:ss format.
  * **`$SongTitle`** - The title of the song, as entered in the **T:** field.
  * **`$SongTranscriber`** - Your name, as entered in the **Z:** field.

### Save & Export ###
  * **Prompt to save new Maestro Songs** - When selected, Maestro will prompt you to save the Maestro Song file when you try to close the application or open a different song. This only applies to new songs -- Maestro will always prompt you to save when closing an existing Maestro Song that's been modified.
  * **Always prompt for the ABC file name when exporting** - When selected, Maestro will always ask you for the name of the .abc file when you click Export ABC. If deselected, it'll only prompt you the first time you export the song. Even with this option disabled, you can still pick a new export file using File > Export ABC As... (Ctrl+Shift+E).
