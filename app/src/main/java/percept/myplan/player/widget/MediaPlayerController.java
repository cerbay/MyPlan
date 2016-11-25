/*
 * Copyright 2016 eneim@Eneim Labs, nam@ene.im
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package percept.myplan.player.widget;

import android.widget.MediaController;
import com.google.android.exoplayer.ExoPlayer;

import java.util.ArrayList;
import java.util.List;

import percept.myplan.media.Cineer;

/**
 * Created by eneim on 6/23/16.
 */
public class MediaPlayerController implements MediaController.MediaPlayerControl {

  private final Cineer player;

  /**
   * Callbacks which will react to the player pausing or playing.
   */
  final List<PlayerControlCallback> callbacks;

  public MediaPlayerController(Cineer player) {
    this.player = player;
    this.callbacks = new ArrayList<>();
  }

  @Override public boolean canPause() {
    return true;
  }

  @Override public boolean canSeekBackward() {
    return true;
  }

  @Override public boolean canSeekForward() {
    return true;
  }

  /**
   * This is an unsupported operation.
   * <p>
   * Application of audio effects is dependent on the audio renderer used. When using
   * {@link com.google.android.exoplayer.MediaCodecAudioTrackRenderer}, the recommended approach is
   * to extend the class and override
   * {@link com.google.android.exoplayer.MediaCodecAudioTrackRenderer#onAudioSessionId}.
   *
   * @throws UnsupportedOperationException Always thrown.
   */
  @Override public int getAudioSessionId() {
    throw new UnsupportedOperationException();
  }

  @Override public int getBufferPercentage() {
    return player.getBufferedPercentage();
  }

  @Override public int getCurrentPosition() {
    return player.getDuration() == ExoPlayer.UNKNOWN_TIME ? 0 : (int) player.getCurrentPosition();
  }

  @Override public int getDuration() {
    return player.getDuration() == ExoPlayer.UNKNOWN_TIME ? 0 : (int) player.getDuration();
  }

  @Override public boolean isPlaying() {
    return player.isPlaying();
  }

  @Override public void start() {
    player.start();
    for (PlayerControlCallback callback : callbacks) {
      callback.onPlay();
    }
  }

  @Override public void pause() {
    player.pause();
    for (PlayerControlCallback callback : callbacks) {
      callback.onPause();
    }
  }

  @Override public void seekTo(int timeMillis) {
    long seekPosition = player.getDuration() == ExoPlayer.UNKNOWN_TIME ? 0
        : Math.min(Math.max(0, timeMillis), getDuration());
    player.seekTo(seekPosition);
  }

  // Support observable mechanism
  /**
   * Add a callback to listen to play and pause events.
   *
   * @param callback Responds when the player is paused or played.
   */
  public void addCallback(PlayerControlCallback callback) {
    callbacks.add(callback);
  }

  /**
   * Remove a callback which is currently listening to play and pause events on the
   * {@link com.google.android.exoplayer.ExoPlayer} instance.
   *
   * @param callback Responds when the player is paused or played.
   */
  public void removeCallback(PlayerControlCallback callback) {
    callbacks.remove(callback);
  }
}
