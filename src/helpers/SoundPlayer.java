package helpers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

// Class that handles the playing of WAV, MIDI, and MP3 audio files
// for game sound effects and music
public class SoundPlayer {
	
	private static Sequencer midiSequence = null;
	private static InputStream musicStream;
	
	public static void cacheSoundPlaying() {
		SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_ALERT.wav"), -80.0f);
	}
	
//	public static Clip loadSound(String wavPath) {
//		try {
//	    	File wavFile = new File(wavPath);
//	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(wavFile);
//	        Clip clip = AudioSystem.getClip();
//	        clip.open(audioInputStream);
//	        return clip;
//	    } catch(Exception ex) {
//	        System.out.println("Error with loading sound: " + wavPath);
//	        ex.printStackTrace();
//	        return null;
//	    }
//	}
	
	// Play a WAV audio file at default volume
	public static void playWAV(String wavPath) {
		SoundPlayer.playWAV(wavPath, 0.0f);
	}
	
	// Play WAV audio file with given gain (increases or decreases volume)
	public static void playWAV(String wavPath, float gain) {
		if((gain > 6.0f) || (gain < -80.0f)) {
			System.out.println("Gain can only be between -80f and 6.0f: Your Gain = " + Float.toString(gain));
			return;
		}
		
	    try {
	    	File wavFile = new File(wavPath);
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(wavFile);
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        if(gain != 0.0f) {
				FloatControl gainControl = 
					    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
					gainControl.setValue(gain); // Reduce volume by set gain
	        }
	        
	        // Start the clip first
	        clip.start();
	        
	        // Add listener to close clip when it is done
	        LineListener listener = new LineListener() {
	            public void update(LineEvent event) {
	                    if (event.getType() == LineEvent.Type.STOP) {
	                    	clip.close();
	                        return;
	                    }
	            }
	        };
	        clip.addLineListener(listener);
	    } catch(Exception ex) {
	        System.out.println("Error with playing sound: " + wavPath);
	        ex.printStackTrace();
	    }
	}
	
	/// Midi play with set volume for each channel
	public static void playMidi(String midiPath, int volume) {
		try {
			// Set Synthesizer and set loop parameter if not already done
			if(SoundPlayer.midiSequence == null) {
				SoundPlayer.midiSequence = MidiSystem.getSequencer();
				SoundPlayer.midiSequence.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			}
			
			// Calculate gain
			double gain = (double) volume / 100.0;
	        
	        // Opens the device, indicating that it should now acquire any
	        // system resources it requires and become operational.
			SoundPlayer.midiSequence.open();
	        
	        // Create a stream from a file
	        SoundPlayer.musicStream = new BufferedInputStream(new FileInputStream(new File(midiPath)));

	        // Sets the current sequence on which the sequencer operates.
	        // The stream must point to MIDI file data.
	        SoundPlayer.midiSequence.setSequence(SoundPlayer.musicStream);
	        
	        // Get the tracks for the sequence and set their volume to the specified level
			Track[] tracks = SoundPlayer.midiSequence.getSequence().getTracks();
			for(Track track : tracks) {
				for(int x = 0; x < 16; x++) {
					
					track.add(new MidiEvent(
							new ShortMessage(ShortMessage.CONTROL_CHANGE, x, 7, (int)(gain*127)), 0));
				}
			}
	 
	        // Starts playback of the MIDI data in the currently loaded sequence.
			SoundPlayer.midiSequence.start();
		} catch (Exception e) {
			// Print error message
			System.out.println("Issue playing Midi stream");
			e.printStackTrace();
		}
	}
	
	// Play Midi without volume set
	public static void playMidi(String midiPath) {
		try {
			// Set Synthesizer and set loop parameter if not already done
			if(SoundPlayer.midiSequence == null) {
				SoundPlayer.midiSequence = MidiSystem.getSequencer();
				SoundPlayer.midiSequence.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			}
	        
	        // Opens the device, indicating that it should now acquire any
	        // system resources it requires and become operational.
			SoundPlayer.midiSequence.open();
	        
	        // Create a stream from a file
	        SoundPlayer.musicStream = new BufferedInputStream(new FileInputStream(new File(midiPath)));

	        // Sets the current sequence on which the sequencer operates.
	        // The stream must point to MIDI file data.
	        SoundPlayer.midiSequence.setSequence(SoundPlayer.musicStream);
	 
	        // Starts playback of the MIDI data in the currently loaded sequence.
	        SoundPlayer.midiSequence.start();
		} catch (Exception e) {
			// Print error message
			System.out.println("Issue playing Midi stream");
			e.printStackTrace();
		}
	}
	
	// Change MIDI background music
	public static void changeMidi(String midiPath) {
		// Stop the current music
		SoundPlayer.stopMidi();
		
		// Play the new music
		SoundPlayer.playMidi(midiPath);
	}
	
	// Change MIDI background music with volume control
	public static void changeMidi(String midiPath, int volume) {
		// Stop the current music
		SoundPlayer.stopMidi();
		
		// Play the new music
		SoundPlayer.playMidi(midiPath, volume);
	}
	
	public static void stopMidi() {
		if(SoundPlayer.midiSequence != null) {
			SoundPlayer.midiSequence.stop();
			try {
				SoundPlayer.musicStream.close();
			} catch (IOException e) {
				// Print error message
				System.out.println("Issue closing Midi stream");
				e.printStackTrace();
			}
		}
	}
	
}
