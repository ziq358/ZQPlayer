//
// Created by johnwu on 2019/4/27.
//

#ifndef ZQPLAYER_PLAYERFRAME_H
#define ZQPLAYER_PLAYERFRAME_H



extern "C" {
#import <libavutil/time.h> //为了引入 uint8_t
#include <libavutil/frame.h>
}

enum PlayerFrameType {
    PlayerFrameTypeNone,
    PlayerFrameTypeVideo,
    PlayerFrameTypeAudio
};

class PlayerFrame {
public:
    PlayerFrameType type = PlayerFrameTypeNone;
    AVFrame  *data = NULL;
    int size;
    double position;
    double duration;
};


#endif //ZQPLAYER_PLAYERFRAME_H
