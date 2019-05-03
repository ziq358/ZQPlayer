// MusicInfo.aidl
package com.zq.playerlib.service;

interface StatusListener {
   void onLoading();
   void onPrepareFinished();
   void onPlaying();
   void onPause();
   void onStop();
   void onError(String msg);
}