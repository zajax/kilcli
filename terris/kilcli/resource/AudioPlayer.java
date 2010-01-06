/*
 *  Copyright (c) 2003 by Jason Baumeister (modified for KilCli use
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the project nor the names of its contributors
 *  may be used to endorse or promote products derived from this software
 *  without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE PROJECT AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE PROJECT OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 */

package terris.kilcli.resource;

import	java.io.File;
import	java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.BufferedInputStream;

import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioInputStream;
import	javax.sound.sampled.AudioSystem;
import	javax.sound.sampled.DataLine;
import	javax.sound.sampled.LineUnavailableException;
import	javax.sound.sampled.Mixer;
import	javax.sound.sampled.SourceDataLine;
import javazoom.jlme.decoder.Decoder;
import javazoom.jlme.decoder.Header;
import javazoom.jlme.decoder.SampleBuffer;
import javazoom.jlme.decoder.BitStream;


public class AudioPlayer
{
	/**	Flag for debugging messages.
	 *	If true, some messages are dumped to the console
	 *	during operation.
	 */
	private static boolean DEBUG = false;
	public static boolean stopped;
	public static SourceDataLine line;
	private static String strMixerName = null;


	private static int DEFAULT_EXTERNAL_BUFFER_SIZE = 128000;
	private static int nExternalBufferSize = DEFAULT_EXTERNAL_BUFFER_SIZE;
	private static int nInternalBufferSize = AudioSystem.NOT_SPECIFIED;


	public static void main(String[] args)
	{

		if (args.length != 1) {
			printUsageAndExit();
		}
		/*
		 *	We make shure that there is only one more argument, which
		 *	we take as the filename of the soundfile we want to play.
		 */

		String strFilename = args[0];

		File soundFile = new File(strFilename);

		/*
		 *	We have to read in the sound file.
		 */
		AudioInputStream audioInputStream = null;
		try
		{
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
			playAudioStream(audioInputStream);
		}
		catch (Exception e)
		{
			/*
			 *	In case of an exception, we dump the exception
			 *	including the stack trace to the console output.
			 *	Then, we exit the program.
			 */
			e.printStackTrace();
			System.exit(1);
		}
		System.exit(0);
	}

	public static void playAudioFile(String fileName) {
		stopped = false;
		File soundFile = new File(fileName);
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
			playAudioStream(audioInputStream);
		} catch (Exception e) {
			System.out.println("Problem with file " + fileName + ":");
			e.printStackTrace();
		}

	}

	public static void playMP3(String fileName) {
		stopped = false;
		try {
			play(new BufferedInputStream(new FileInputStream(fileName), 2048));
		} catch (Exception e) {
			System.out.println("couldn't locate the mp3 file");
		}
	}

  public static void play(InputStream stream) throws Exception {
	BitStream bitstream = new BitStream(stream);
	stopped = false;
    boolean first = true;
    int length;
    Header header = bitstream.readFrame();
    Decoder decoder = new Decoder(header, bitstream);
    while (!stopped)
    {
      try
      {
      	SampleBuffer output = (SampleBuffer)decoder.decodeFrame();
      	length = output.size();
      	if (length == 0) break;
      	//{
      		if (first)
      		{
      		  first = false;
      		  System.out.println("frequency: "+decoder.getOutputFrequency() + ", channels: " + decoder.getOutputChannels());
      		  startOutput(new AudioFormat(decoder.getOutputFrequency(), 16, decoder.getOutputChannels(), true, false));
      		}
      		line.write(output.getBuffer(), 0, length);
      		bitstream.closeFrame();
      		header = bitstream.readFrame();
      		//System.out.println("Mem:"+(rt.totalMemory() - rt.freeMemory())+"/"+rt.totalMemory());
      	//}
	  } catch (Exception e)
	  	{
			//e.printStackTrace();
			break;
		}
    }
    bitstream.close();
  }

  public static void startOutput(AudioFormat playFormat) throws LineUnavailableException {
    DataLine.Info info= new DataLine.Info(SourceDataLine.class, playFormat);

    if (!AudioSystem.isLineSupported(info)) {
      throw new LineUnavailableException("sorry, the sound format cannot be played");
    }
    line = (SourceDataLine)AudioSystem.getLine(info);
    line.open(playFormat);
    line.start();
  }

	public static void playAudioStream(AudioInputStream audioInputStream) {

		/*
		 *	From the AudioInputStream, i.e. from the sound file,
		 *	we fetch information about the format of the
		 *	audio data.
		 *	These information include the sampling frequency,
		 *	the number of
		 *	channels and the size of the samples.
		 *	These information
		 *	are needed to ask Java Sound for a suitable output line
		 *	for this audio file.
		 */
		AudioFormat	audioFormat = audioInputStream.getFormat();
		if (DEBUG)
		{
			System.out.println("AudioPlayer.main(): format: " + audioFormat);
		}


		line = getSourceDataLine(strMixerName, audioFormat, nInternalBufferSize);
		// second chance for compression formats.
		if (line == null)
		{
			AudioFormat	sourceFormat = audioFormat;
			AudioFormat	targetFormat = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED,
				sourceFormat.getSampleRate(),
				16,
				sourceFormat.getChannels(),
				sourceFormat.getChannels() * 2,
				sourceFormat.getSampleRate(),
				false);
			if (DEBUG)
			{
				System.out.println("AudioPlayer.<init>(): source format: " + sourceFormat);
				System.out.println("AudioPlayer.<init>(): target format: " + targetFormat);
			}
			audioInputStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
			audioFormat = audioInputStream.getFormat();
			if (DEBUG)
			{
				System.out.println("AudioPlayer.<init>(): received target format: " + audioFormat);
			}
			line = getSourceDataLine(strMixerName, audioFormat, nInternalBufferSize);
		}

		if (line == null)
		{
			System.out.println("AudioPlayer: cannot get SourceDataLine for format " + audioFormat);
			System.exit(1);
		}

		/*
		 *	Still not enough. The line now can receive data,
		 *	but will not pass them on to the audio output device
		 *	(which means to your sound card). This has to be
		 *	activated.
		 */
		line.start();

		/*
		 *	Ok, finally the line is prepared. Now comes the real
		 *	job: we have to write data to the line. We do this
		 *	in a loop. First, we read data from the
		 *	AudioInputStream to a buffer. Then, we write from
		 *	this buffer to the Line. This is done until the end
		 *	of the file is reached, which is detected by a
		 *	return value of -1 from the read method of the
		 *	AudioInputStream.
		 */
		int	nBytesRead = 0;
		byte[]	abData = new byte[nExternalBufferSize];
		while (nBytesRead != -1)
		{
			try
			{
				nBytesRead = audioInputStream.read(abData, 0, abData.length);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			if (DEBUG)
			{
				System.out.println("AudioPlayer.main(): read from AudioInputStream (bytes): " + nBytesRead);
			}
			if (nBytesRead >= 0)
			{
				int	nBytesWritten = line.write(abData, 0, nBytesRead);
				if (DEBUG)
				{
					System.out.println("AudioPlayer.main(): written to SourceDataLine (bytes): " + nBytesWritten);
				}
			}
		}

		/*
		 *	Wait until all data is played.
		 *	This is only necessary because of the bug noted below.
		 *	(If we do not wait, we would interrupt the playback by
		 *	prematurely closing the line and exiting the VM.)
		 *
		 *	Thanks to Margie Fitch for bringing me on the right
		 *	path to this solution.
		 */
		if (DEBUG)
		{
			System.out.println("AudioPlayer.main(): before drain");
		}
		line.drain();

		/*
		 *	All data are played. We can close the shop.
		 */
		if (DEBUG)
		{
			System.out.println("AudioPlayer.main(): before close");
		}
		line.close();

		/*
		 *	There is a bug in the Sun jdk1.3.
		 *	It prevents correct termination of the VM.
		 *	So we have to exit ourselves.
		 */
		if (DEBUG)
		{
			System.out.println("AudioPlayer.main(): before exit");
		}
	}


	private static void printUsageAndExit()
	{
		System.out.println("AudioPlayer: usage:");
		System.out.println("\tjava AudioPlayer -l");
		System.out.println("\tjava AudioPlayer [-M <mixername>] <soundfile>");
		System.exit(1);
	}

	/*
	 *	This method tries to return a Mixer.Info whose name
	 *	matches the passed name. If no matching Mixer.Info is
	 *	found, null is returned.
	 */
	private static Mixer.Info getMixerInfo(String strMixerName)
	{
		Mixer.Info[]	aInfos = AudioSystem.getMixerInfo();
		for (int i = 0; i < aInfos.length; i++)
		{
			if (aInfos[i].getName().equals(strMixerName))
			{
				return aInfos[i];
			}
		}
		return null;
	}

	private static SourceDataLine getSourceDataLine(String strMixerName,
							AudioFormat audioFormat,
							int nBufferSize)
	{
		/*
		 *	Asking for a line is a rather tricky thing.
		 *	We have to construct an Info object that specifies
		 *	the desired properties for the line.
		 *	First, we have to say which kind of line we want. The
		 *	possibilities are: SourceDataLine (for playback), Clip
		 *	(for repeated playback)	and TargetDataLine (for
		 *	 recording).
		 *	Here, we want to do normal playback, so we ask for
		 *	a SourceDataLine.
		 *	Then, we have to pass an AudioFormat object, so that
		 *	the Line knows which format the data passed to it
		 *	will have.
		 *	Furthermore, we can give Java Sound a hint about how
		 *	big the internal buffer for the line should be. This
		 *	isn't used here, signaling that we
		 *	don't care about the exact size. Java Sound will use
		 *	some default value for the buffer size.
		 */
		SourceDataLine	line = null;
		DataLine.Info	info = new DataLine.Info(SourceDataLine.class,
							 audioFormat, nBufferSize);
		try
		{
			if (strMixerName != null)
			{
				Mixer.Info	mixerInfo = getMixerInfo(strMixerName);
				if (mixerInfo == null)
				{
					System.out.println("AudioPlayer: mixer not found: " + strMixerName);
					System.exit(1);
				}
				Mixer	mixer = AudioSystem.getMixer(mixerInfo);
				line = (SourceDataLine) mixer.getLine(info);
			}
			else
			{
				line = (SourceDataLine) AudioSystem.getLine(info);
			}

			/*
			 *	The line is there, but it is not yet ready to
			 *	receive audio data. We have to open the line.
			 */
			line.open(audioFormat, nBufferSize);
		}
		catch (LineUnavailableException e)
		{
			if (DEBUG)
			{
				e.printStackTrace();
			}
		}
		catch (Exception e)
		{
			if (DEBUG)
			{
				e.printStackTrace();
			}
		}
		return line;
	}

	public static void stop() {
		stopped = true;
		if (line != null) {
			line.drain();
			line.close();
		}
	}
}
