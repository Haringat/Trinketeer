#include "targaloader.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>
#include <stdbool.h>

void loadTextureData(char *path, targafile *tga){
    printf("loading texture data from %s\n", path);
    FILE *texfile = fopen(path, "r");
    printf("releasing temporary resources\n");
    uint8_t head[18];
    printf("loading file header\n");
    fread(&head, 18, 1, texfile);
    printf("scanning loaded header\n");
    tga->picidlength                        = head[0];
    uint8_t paltype                         = head[1];
    uint8_t pictype                         = head[2];
    uint16_t paloffset                      = head[3];
    *(((uint8_t *)(&paloffset))+1)          = head[4];
    uint8_t pallength                       = head[5];
    *(((uint8_t *)(&pallength))+1)          = head[6];
    uint8_t colorsize                       = head[7];
    uint16_t zerox                          = head[8];
    *(((uint8_t *)(&zerox))+1)              = head[9];
    uint16_t zeroy                          = head[10];
    *(((uint8_t *)(&zeroy))+1)              = head[11];
    tga->picwidth                           = head[12];
    *(((uint8_t *)(&(tga->picwidth)))+1)    = head[13];
    tga->picheight                          = head[14];
    *(((uint8_t *)(&(tga->picheight)))+1)   = head[15];
    tga->bpp                                = head[16];
    uint8_t attributes                      = head[17];
    uint8_t attbpp                          =  attributes & 0b00001111;
    uint8_t zeroh                           = (attributes & 0b00010000) >> 4;
    uint8_t zerov                           = (attributes & 0b00100000) >> 5;
    printf("scan results:\n");
    printf("picture ID is %d bytes long\n", tga->picidlength);
    switch(paltype){
    case 0:
        printf("no color palette\n");
        break;
    case 1:
        printf("color palette detected\n");
        break;
    default:
        printf("invalid color palette format. Aborting...\n");
        return;
    }
    bool compressed = (pictype & 0b1000) > 0 ? true : false;
    int format = pictype & 0b11;
    if(format == 0){
        printf("no picture data detected(this is not an error)\n");
    } else {
        if(compressed == true){
            printf("compression detected\n");
        } else {
            printf("no compression detected\n");
        }
        if(format == 1){
            printf("color palette detected\n");
        }
        if(format == 2){
            printf("RGB format detected\n");
        }
        if(format == 3){
            printf("monochrome format detected\n");
        }
    }
    printf("the point of origin is shifted by %d|%d\n", zerox, zeroy);
    printf("the image is %dx%d pixel\n", tga->picwidth, tga->picheight);
    printf("the image has %d bpp\n", tga->bpp);
    printf("each pixel contains %d attribute bits\n", attbpp);
    printf("the origin is in the %s %s corner\n",
            (zerov == 1) ? "upper" : "lower", (zeroh == 1) ? "right" : "left");
    uint8_t colorpalette[pallength];
    tga->buffer = malloc(tga->picheight*tga->picwidth*sizeof(uint32_t));
    if(tga->picidlength > 0 ){
        tga->picid = malloc(tga->picidlength*sizeof(uint8_t));
        printf("reading picture ID\n");
        fread(tga->picid, sizeof(uint8_t), tga->picidlength, texfile);
        printf("finished reading picture ID\n");
    } else {
        printf("skipping picture ID(not present)\n");
    }
    if(paltype > 0){
        printf("reading color palette\n");
        fseek(texfile, paloffset, SEEK_CUR);
        fread(colorpalette, colorsize, pallength - paloffset, texfile);
        printf("finished reading color palette\n");
    } else {
        printf("skipping color palette(not present)\n");
    }
    if(format == 0){
        printf("skipping pixel data\n");
    }else{
        if(compressed == true){
            for(int row = 0; row < tga->picheight; row++){
                int curcol = 0;
                int totalindex = 0;
                uint8_t controlbyte = 0;
                uint32_t pixel = 0;
                while(curcol < tga->picwidth){
                    controlbyte = fgetc(texfile);
                    if((controlbyte & 0b00000001) == 0){
                        fread(&(tga->buffer[totalindex]), tga->bpp / 8,
                                (controlbyte >> 1), texfile );
                        curcol += (controlbyte >> 1);
                        totalindex += (controlbyte >> 1);
                    } else {
                        fread( &pixel, tga->bpp / 8, 1, texfile );
                        for( uint8_t i = 0; i < (controlbyte >> 1); i++ ){
                            tga->buffer[totalindex] = pixel;
                        }
                        curcol += (controlbyte >> 1) + 1;
                        totalindex += (controlbyte >> 1) + 1;
                    }
                }
            }
        } else {
            switch(tga->bpp){
                case 1: {
                    uint8_t *pixelbuffer = malloc(tga->picwidth * tga->picheight / 8
                            * sizeof(uint8_t));
                    fread(pixelbuffer, sizeof(uint8_t),
                            tga->picwidth * tga->picheight / 8, texfile);
                    uint32_t white = 0b11111111111111111111111111111111;
                    uint32_t black = 0b11111111000000000000000000000000;
                    for(int i = 0; i < tga->picwidth * tga->picheight; i++){
                        if((( pixelbuffer[( i / 8 ) + ( i % 8 )] >> i % 8 )
                                & 0b00000001) == 1){
                            tga->buffer[i] = white;
                        } else {
                            tga->buffer[i] = black;
                        }
                    }
                    free(pixelbuffer);
                    break;
                }case 8:{
                    printf("not yet implemented\n");
                    break;
                }case 15:{
                    uint16_t *pixelbuffer = malloc(tga->picwidth *
                            tga->picheight * sizeof(uint16_t));
                    fread(pixelbuffer, sizeof(uint16_t), tga->picwidth *
                            tga->picheight, texfile);
                    uint32_t buffer;
                    for(int i = 0; i < tga->picwidth * tga->picheight; i++){
                        uint8_t blue  = (  pixelbuffer[i] & 0b0000000000011111 )
                                * 256 / 32;
                        uint8_t green = (( pixelbuffer[i] & 0b0000001111100000 )
                                >>  5) * 256 / 32;
                        uint8_t red   = (( pixelbuffer[i] & 0b0111110000000000 )
                                >> 10) * 256 / 32;
                        uint8_t alpha = (( pixelbuffer[i] & 0b1000000000000000 )
                                >> 15) * 255;
                        buffer = blue | ( green << 8 ) | ( red << 16 )
                                | ( alpha << 24 );
                        tga->buffer[i] = buffer;
                    }
                    free(pixelbuffer);
                    break;
                }case 16:{
                    uint16_t *pixelbuffer = malloc(tga->picwidth * 
                            tga->picheight * sizeof(uint16_t));
                    fread(pixelbuffer, sizeof(uint16_t), tga->picwidth *
                            tga->picheight, texfile);
                    uint32_t buffer;
                    for(int i = 0; i < tga->picwidth * tga->picheight; i++){
                        uint8_t blue  = (  pixelbuffer[i] & 0b0000000000011111 )
                                * 256 / 32;
                        uint8_t green = (( pixelbuffer[i] & 0b0000011111100000 )
                                >>  5) * 256 / 64;
                        uint8_t red   = (( pixelbuffer[i] & 0b1111100000000000 )
                                >> 10) * 256 / 32;
                        uint8_t alpha = 255;
                        buffer = blue | ( green << 8 ) | ( red << 16 )
                                | ( alpha << 24 );
                        tga->buffer[i] = buffer;
                    }
                    free(pixelbuffer);
                    break;
                }case 24:{
                    uint8_t *pixelbuffer = malloc(tga->picwidth *
                            tga->picheight * 3 * sizeof(uint8_t));
                    fread(pixelbuffer, sizeof(uint8_t),
                            tga->picwidth * tga->picheight * 3, texfile);
                    uint32_t buffer;
                    for(int i = 0; i < tga->picwidth * tga->picheight; i++){
                        uint8_t blue  = pixelbuffer[i * 3];
                        uint8_t green = pixelbuffer[i * 3 + 1];
                        uint8_t red   = pixelbuffer[i * 3 + 2];
                        uint8_t alpha = 255;
                        buffer = blue | ( green << 8 ) | ( red << 16 )
                                | ( alpha << 24 );
                        tga->buffer[i] = buffer;
                    }
                    break;
                }case 32:{
                    printf("yay! I can just copy+paste:D\n");
                    fread(tga->buffer, sizeof(uint32_t),
                            tga->picwidth * tga->picheight, texfile);
                    break;
                }
            }
        }
    }
    if(zerov == 0){
        printf("reverting line order\n");
        uint32_t buffer[tga->picwidth];
        for(int i = 0; i < tga->picheight / 2; i++){
            memcpy(&buffer, &(tga->buffer[i * tga->picwidth]),
                    tga->picwidth * sizeof(uint32_t));
            memcpy(&(tga->buffer[i * tga->picwidth]), &(tga->buffer[( tga->picheight * tga->picwidth )
                    - ( i + 1 ) * tga->picwidth]), tga->picwidth * sizeof(uint32_t));
            memcpy(&(tga->buffer[( tga->picheight * tga->picwidth ) - ( i + 1 ) * tga->picwidth]),
                    &buffer, tga->picwidth * sizeof(uint32_t));
        }
    } else {
        printf("keeping line order\n");
    }
    if(zeroh == 1){
        printf("reverting lines\n");
        uint32_t buffer;
        for( int i = 0; i < tga->picheight; i++ ){
            for( int j = 0; j < tga->picwidth; j++ ){
                buffer = tga->buffer[( i * tga->picwidth ) + j];
                tga->buffer[( i * tga->picwidth ) + j] = tga->buffer[( i * tga->picwidth )
                        + tga->picwidth - ( j + 1 )];
                tga->buffer[( i * tga->picwidth ) + tga->picwidth - ( j + 1 )] = buffer;
            }
        }
    } else {
        printf("keeping bytes in order\n");
    }
}
