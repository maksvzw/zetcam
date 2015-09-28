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
package org.maksvzw.zetcam.core.audio.filters;

import com.xuggle.mediatool.IMediaListener;
import com.xuggle.mediatool.event.IAddStreamEvent;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.mediatool.event.ICloseCoderEvent;
import com.xuggle.mediatool.event.ICloseEvent;
import com.xuggle.mediatool.event.IFlushEvent;
import com.xuggle.mediatool.event.IOpenCoderEvent;
import com.xuggle.mediatool.event.IOpenEvent;
import com.xuggle.mediatool.event.IReadPacketEvent;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.mediatool.event.IWriteHeaderEvent;
import com.xuggle.mediatool.event.IWritePacketEvent;
import com.xuggle.mediatool.event.IWriteTrailerEvent;
import com.xuggle.xuggler.IAudioSamples;
import java.time.Duration;
import org.maksvzw.zetcam.core.MediaFilter;
import org.maksvzw.zetcam.core.MediaType;

/**
 *
 * @author Lenny Knockaert
 */
public abstract class AudioFilter extends MediaFilter<IAudioSamples> implements IMediaListener
{
    @Override
    public MediaType getType() {
        return MediaType.AUDIO;
    }

    @Override
    public void onVideoPicture(IVideoPictureEvent ivpe) { }

    @Override
    public void onAudioSamples(IAudioSamplesEvent iase)
    {
        this.onFilter(iase.getAudioSamples());
    }

    @Override
    public void onOpen(IOpenEvent ioe) { }

    @Override
    public void onClose(ICloseEvent ice) { }

    @Override
    public void onAddStream(IAddStreamEvent iase) { }

    @Override
    public void onOpenCoder(IOpenCoderEvent ioce) { }

    @Override
    public void onCloseCoder(ICloseCoderEvent icce) { }

    @Override
    public void onReadPacket(IReadPacketEvent irpe) { }

    @Override
    public void onWritePacket(IWritePacketEvent iwpe) { }

    @Override
    public void onWriteHeader(IWriteHeaderEvent iwhe) { }

    @Override
    public void onFlush(IFlushEvent ife) { }

    @Override
    public void onWriteTrailer(IWriteTrailerEvent iwte) { }
}