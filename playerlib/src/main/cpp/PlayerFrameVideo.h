//
// Created by johnwu on 2019/4/28.
//

#ifndef ZQPLAYER_PLAYERFRAMEVIDEO_H
#define ZQPLAYER_PLAYERFRAMEVIDEO_H


#import "PlayerFrame.h"
extern "C" {
#import <libavutil/pixfmt.h>
}

class PlayerFrameVideo : public PlayerFrame{
public:
    int width;
    int height;
    AVPixelFormat format;
    PlayerFrameVideo();

};


#endif //ZQPLAYER_PLAYERFRAMEVIDEO_H
