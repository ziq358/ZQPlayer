//
// Created by johnwu on 2019/4/28.
//

#include <libavutil/pixfmt.h>
#include "PlayerFrameVideo.h"

PlayerFrameVideo::PlayerFrameVideo() {
    type = PlayerFrameTypeVideo;
    format = AV_PIX_FMT_RGBA;
}
