package com.zq.playerlib

interface StatusListener{
        fun onLoading(): Unit {}

        fun onPrepareFinished(): Unit {}

        fun onPlaying(): Unit {}

        fun onPause(): Unit {}

        fun onStop(): Unit {}

        fun onError(msg: String): Unit {}
    }