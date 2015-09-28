/* 
 * Copyright (C) 2015 Lenny Knockaert <lknockx@gmail.com>
 *
 * This file is part of ZetCam
 *
 * ZetCam is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ZetCam is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.maksvzw.zetcam;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import org.maksvzw.zetcam.gui.MainWindow;

/**
 *
 * @author Lenny
 */
public class ZetCam 
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        MainWindow window = new MainWindow();
        window.setVisible(true);
        
        try {
            File audioFile = new File("C:\\Users\\Lenny\\Desktop\\Black Sabbath - Killing Yourself To Live.mp3");
            File audioFile2 = new File("C:\\Users\\Lenny\\Desktop\\Yesterday.mp3");
            File audioFile3 = new File("C:\\Users\\Lenny\\Desktop\\porsche.wav");
            File audioFile4 = new File("C:\\Users\\Lenny\\Desktop\\Warrant - Cherry Pie.mp3");
            File audioFile5 = new File("C:\\Users\\Lenny\\Desktop\\naim-test-2-wav-24-96000.wav");
            File audioFile6 = new File("C:\\Users\\Lenny\\Desktop\\Media-Convert_test3_PCM_Stereo_VBR_16SS_11025Hz.wav");

            /*BufferedImage[] images = new BufferedImage[]
            {
                ImageIO.read(new File("C:\\Users\\Lenny\\Desktop\\test.jpg")),
                ImageIO.read(new File("C:\\Users\\Lenny\\Desktop\\test2.jpg"))
            };
            TweenTransition t = new TweenTransition(Image.createAndFill(
                    images[0].getWidth(), images[0].getHeight(), images[0].getType(), Color.BLACK), images[0], 7);
            
            int i = 0;
            while(t.hasRemaining()) {
                ImageIO.write(t.nextTransition(), "png", new File("C:\\Users\\Lenny\\Desktop\\testTransition"+i+".png"));
                i++;
            }*/
            //testImageOperations();
            //ZipTest.run(Paths.get("C:\\Users\\Lenny\\Desktop\\test.zip"));
            
            /*try (AudioSource ai = new AudioSource(audioFile3);
                 AudioSource ai2 = new AudioSource(audioFile5);
                 AudioSource ai3 = new AudioSource(audioFile6)) 
            {
                //ai2.addListener(new VolumeFilter(1.5));
                //ai3.addListener(new VolumeFilter(0.5));
                //ai3.addListener(new FadeFilter(Duration.ZERO, Duration.ofSeconds(5), CurveType.EXP, FadeDirection.IN));
                
                try(AudioMixer mixer = new AudioMixer())
                {
                    mixer.link(0, ai);
                    mixer.link(1, ai2);
                    mixer.addListener(new PlaybackFilter());
                    while(mixer.getNumActiveInputs() > 0)
                        mixer.read(8192);
                    mixer.unlink(0);
                    mixer.unlink(1);
                }
                
                ai2.open();
                //System.out.println(ai.getInfo());
                //ai.addListener(new VolumeFilter(0.05));
                //ai2.addListener(new FadeInFilter(Duration.ZERO, Duration.ofSeconds(5), CurveType.EXP));
                ai2.addListener(new TrimFilter(Duration.ofSeconds(2), Duration.ofSeconds(6)));
                ai2.addListener(new PlaybackSink());
                ai2.readAll();
                //AudioDecodeTest.run(audioFile2);
                //testImageTransformations();
                //testImageOperations();
                //encodeAudio(audioFile5, new File("C:\\Users\\Lenny\\Desktop\\test.aac"));
                //encodeVideo(new File("C:\\Users\\Lenny\\Desktop\\test5.mp4"));
                //showInfo();
                //play(audioFile2);
            }*/
        } catch (Exception ex) {
            Logger.getLogger(ZetCam.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void encodeVideo(File outputFile) 
    {
        /*VideoEncoding encoding = new VideoEncoding.Builder()
                .useEncoder(CodecID.H264)
                .setFrameRate(2.0)
                .setGOPSize(10)
                .setBitRate(8 * 1024 * 1024 * 8)
                .setWidth(frames[0].getFormat().getWidth())
                .setHeight(frames[0].getFormat().getHeight())
                .setPixelFormat(PixelFormat.YUV420P)
                .setOption("crf", "0")
                .setOption("preset", "fast")
                //.setOption("profile", "main")
                //.setOption("maxrate", "8192"),
                //.setOption("minrate", "8192"),
                .build();
        */
    }
    
    private static void encodeAudio(File inputFile, File outputFile)
    {
        /*AudioEncoding encoding = new AudioEncoding.Builder()
                .useEncoder(CodecID.AAC)
                .setSampleRate(48000)
                .setSampleFormat(SampleFormat.S16)
                .setChannelCount(2)
                .setChannelLayout(ChannelLayout.STEREO)
                .setQuality(CodecQuality.fromScale(9))
                .setBitRate(192 * 1024 * 8)
                .build();*/
    }
    
    private static void showInfo() throws LineUnavailableException 
    {
        Mixer.Info[] mi = AudioSystem.getMixerInfo();
        for (Mixer.Info info : mi) {
            System.out.println("info: " + info);
            Mixer m = AudioSystem.getMixer(info);
            System.out.println("mixer " + m);
            Line.Info[] sl = m.getSourceLineInfo();
            for (Line.Info info2 : sl) {
                System.out.println("    info: " + info2);
                Line line = AudioSystem.getLine(info2);
                if (line instanceof SourceDataLine) {
                    SourceDataLine source = (SourceDataLine) line;

                    DataLine.Info i = (DataLine.Info) source.getLineInfo();
                    for (javax.sound.sampled.AudioFormat format : i.getFormats()) {
                        System.out.println("    format: " + format);
                    }
                }
            }
        }
    }
    
    private static String formatDuration(Duration d) {
        long ms = d.toMillis();
        long second = (ms / 1000) % 60;
        long minute = (ms / (1000 * 60)) % 60;
        long hour = (ms / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d.%d", hour, minute, second, ms - (second * 1000));
    }
}
