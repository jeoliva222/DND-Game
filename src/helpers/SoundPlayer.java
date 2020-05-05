package helpers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

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

/**
 * Class that handles the playing of WAV and MIDI audio files
 * for game sound effects and music
 * @author jeoliva
 */
public class SoundPlayer {
	
	private static ScheduledExecutorService clipExecutor = createClipExecutor();
	private static Sequencer midiSequence = null;
	private static InputStream musicStream;
	
	/**
	 * Small helper function to "cache" sound playing logic. 
	 * Called at the start of the application once.
	 */
	public static void cacheSoundPlaying() {
		SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_ALERT.wav"), -80.0f);
	}
	
    private static ScheduledExecutorService createClipExecutor() {
	    return Executors.newScheduledThreadPool(4, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread thread = Executors.defaultThreadFactory().newThread(r);
				thread.setDaemon(true);
				return thread;
			}
		});
    }
	
	/**
	 * Play a WAV audio file at default volume
	 * @param wavPath File path of audio clip
	 */
	public static void playWAV(String wavPath) {
		SoundPlayer.playWAV(wavPath, 0.0f);
	}
	
	/**
	 * Play WAV audio file with given gain (increases or decreases volume)
	 * @param wavPath File path of audio clip
	 * @param gain Float determining gain of audio clip. Accepts values [-80.0f to 6.0f]
	 */
	public static void playWAV(String wavPath, float gain) {
		// Check for out-of-range gain level
		// If out-of-range, reduce to 0.0f;
		if ((gain > 6.0f) || (gain < -80.0f)) {
			System.out.println("'" + wavPath + "' : Gain can only be between -80f and 6.0f: Your Gain = " + Float.toString(gain));
			gain = 0.0f;
		}
		
		// Finalize variables
		final String finalWavPath = wavPath;
		final float finalGain = gain;
		
		// Execute sound loading and playing outside EDT thread
		Runnable clipTask = new Runnable() {
			@Override
			public void run() {
			    try {
			    	File wavFile = new File(finalWavPath);
			        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(wavFile);
			        final Clip clip = AudioSystem.getClip();
			        clip.open(audioInputStream);
			        if (finalGain != 0.0f) {
						FloatControl gainControl = 
							    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
							gainControl.setValue(finalGain); // Reduce volume by set gain
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
			    } catch (Exception ex) {
			        System.out.println("Error with playing sound: '" + finalWavPath + "'");
			        ex.printStackTrace();
			    }
			}
		};
		clipExecutor.execute(clipTask);
	}
	
	/**
	 * Play MIDI with set volume for each channel
	 * @param midiPath File path of the MIDI
	 * @param volume Volume to play MIDI channels at
	 */
	public static void playMidi(String midiPath, int volume) {
		// If the requested volume is full, don't modify the volume of the tracks
		if (volume >= 100) {
			System.out.println("Reverting to standard playing volumes");
			SoundPlayer.playMidi(midiPath);
			return;
		}
		
		// Finalize variables
		final String finalMidiPath = midiPath;
		final float finalVolume = volume;
		
		// Execute MIDI loading and playing outside EDT thread
		Runnable midiTask = new Runnable() {
			@Override
			public void run() {
				try {
					// Set Synthesizer and set loop parameter if not already done
					if (SoundPlayer.midiSequence == null) {
						SoundPlayer.midiSequence = MidiSystem.getSequencer();
						SoundPlayer.midiSequence.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
					}
					
					// Calculate gain
					double gain = (double) finalVolume / 100.0;
			        
			        // Opens the device, indicating that it should now acquire any
			        // system resources it requires and become operational.
					SoundPlayer.midiSequence.open();
			        
			        // Create a stream from a file
			        SoundPlayer.musicStream = new BufferedInputStream(new FileInputStream(new File(finalMidiPath)));
		
			        // Sets the current sequence on which the sequencer operates.
			        // The stream must point to MIDI file data.
			        SoundPlayer.midiSequence.setSequence(SoundPlayer.musicStream);
			        
			        // Get the tracks for the sequence and set their volume to the specified level
					Track[] tracks = SoundPlayer.midiSequence.getSequence().getTracks();
					for (Track track : tracks) {
						for (int x = 0; x < 16; x++) {
							track.add(new MidiEvent(
									new ShortMessage(ShortMessage.CONTROL_CHANGE, x, 7, (int)(gain*127)), 0));
						}
					}
			 
			        // Starts playback of the MIDI data in the currently loaded sequence.
					SoundPlayer.midiSequence.start();
				} catch (Exception ex) {
					// Print error message
					System.out.println("Issue playing Midi stream: '" + finalMidiPath + "'");
					ex.printStackTrace();
				}
			}
		};
		clipExecutor.execute(midiTask);
	}
	
	/**
	 * Play MIDI at default channel volumes
	 * @param midiPath File path of the MIDI
	 */
	public static void playMidi(String midiPath) {
		// Finalize variables
		final String finalMidiPath = midiPath;
		
		// Execute MIDI loading and playing outside EDT thread
		Runnable midiTask = new Runnable() {
			@Override
			public void run() {
				try {
					// Set Synthesizer and set loop parameter if not already done
					if (SoundPlayer.midiSequence == null) {
						SoundPlayer.midiSequence = MidiSystem.getSequencer();
						SoundPlayer.midiSequence.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
					}
			        
			        // Opens the device, indicating that it should now acquire any
			        // system resources it requires and become operational.
					SoundPlayer.midiSequence.open();
			        
			        // Create a stream from a file
			        SoundPlayer.musicStream = new BufferedInputStream(new FileInputStream(new File(finalMidiPath)));
		
			        // Sets the current sequence on which the sequencer operates.
			        // The stream must point to MIDI file data.
			        SoundPlayer.midiSequence.setSequence(SoundPlayer.musicStream);
			 
			        // Starts playback of the MIDI data in the currently loaded sequence.
			        SoundPlayer.midiSequence.start();
				} catch (Exception ex) {
					// Print error message
					System.out.println("Issue playing Midi stream: '" + finalMidiPath + "'");
					ex.printStackTrace();
				}
			}
		};
		clipExecutor.execute(midiTask);
	}
	
	/**
	 * Change the currently playing MIDI
	 * @param midiPath File path of the new MIDI
	 */
	public static void changeMidi(String midiPath) {
		// Stop the current music
		SoundPlayer.stopMidi();
		
		// Play the new music
		SoundPlayer.playMidi(midiPath);
	}
	
	/**
	 * Change the currently playing MIDI, and play the next MIDI with set channel volume
	 * @param midiPath File path of the new MIDI
	 * @param volume Volume to play new MIDI channels at
	 */
	public static void changeMidi(String midiPath, int volume) {
		// Stop the current music
		SoundPlayer.stopMidi();
		
		// Play the new music
		SoundPlayer.playMidi(midiPath, volume);
	}
	
	/**
	 * Stops the currently playing MIDI
	 */
	public static void stopMidi() {
		if (SoundPlayer.midiSequence != null) {
			SoundPlayer.midiSequence.stop();
			try {
				SoundPlayer.musicStream.close();
			} catch (IOException ex) {
				// Print error message
				System.out.println("Error closing Midi stream");
				ex.printStackTrace();
			}
		}
	}
	
}
