#ifndef TARGALOADER_H

#include <stdint.h>

typedef struct {
    uint8_t     picidlength;
    uint16_t    picwidth;
    uint16_t    picheight;
    uint8_t     bpp;
    uint8_t *   picid;
    uint32_t *  buffer;
} targafile;

void loadTextureData(char *path, targafile *file);

#endif
