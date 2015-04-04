// include the c stubs of the exported native method
#include "com_ichmed_trinketeers_savefile_DataLoader.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>
#include <stdbool.h>

JNIEXPORT jbyteArray JNICALL
        Java_com_ichmed_trinketeers_savefile_DataLoader_loadTextureFile
        (JNIEnv *env, jclass thisclass, jstring path){
    printf("entered native textureloader\n");
    jsize pathlen = (*env)->GetStringUTFLength(env, path);
    printf("%d\n", pathlen);
    char *cpath = malloc((pathlen+1) * sizeof(char));
    jboolean iscopy;
    cpath = (char *) (*env)->GetStringUTFChars(env, path, &iscopy);
    cpath[pathlen] = '\0';
    printf("loading texture from %s\n", cpath);
    FILE *texfile = fopen(cpath, "r");
    printf("releasing temporary resources\n");
    (*env)->ReleaseStringUTFChars(env, path, cpath);
    printf("released temporary resources\n");
    uint8_t head[18];
    printf("loading file header\n");
    fread(&head, 18, 1, texfile);
    printf("scanning loaded header\n");
    uint8_t picidsize              = head[0];
    uint8_t paltype                = head[1];
    uint8_t pictype                = head[2];
    uint16_t palstart              = head[3];
    *(((uint8_t *)(&palstart))+1)  = head[4];
    uint16_t pallength             = head[5];
    *(((uint8_t *)(&pallength))+1) = head[6];
    uint8_t colorsize              = head[7];
    uint16_t zerox                 = head[8];
    *(((uint8_t *)(&zerox))+1)     = head[9];
    uint16_t zeroy                 = head[10];
    *(((uint8_t *)(&zeroy))+1)     = head[11];
    uint16_t picwidth              = head[12];
    *(((uint8_t *)(&picwidth))+1)  = head[13];
    uint16_t picheight             = head[14];
    *(((uint8_t *)(&picheight))+1) = head[15];
    uint8_t bpp                    = head[16];
    uint8_t attributes             = head[17];
    uint8_t attbpp                 = (attributes & 0b11110000) >> 4;
    uint8_t zeroh                  = (attributes & 0b00001000) >> 3;
    uint8_t zerov                  = (attributes & 0b00000100) >> 2;
    printf("scan results:\n");
    printf("picture ID is %d bytes long\n", picidsize);
    switch(paltype){
    case 0:
        printf("no color palette\n");
        break;
    case 1:
        printf("color palette detected\n");
        break;
    default:
        printf("invalid color palette format. Aborting...\n");
        jbyteArray ret = (*env)->NewByteArray(env, 0);
        return ret;
    }
    _Bool compressed = (pictype & 0b1000) > 0 ? true : false;
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
    printf("the image is %dx%d pixel\n", picwidth, picheight);
    printf("the image has %d bpp\n", bpp);
    printf("each pixel contains %d attribute bits\n", attbpp);
    printf("the origin is in the %s %s corner\n",
            (zerov == 1) ? "upper" : "lower", (zeroh == 1) ? "right" : "left");
    uint8_t picid[picidsize];
    uint8_t colorpalette[pallength];
    uint32_t *pixeldata = malloc(picheight*picwidth*sizeof(uint32_t));
    if(picidsize > 0 ){
        printf("reading picture ID\n");
        fread(picid, sizeof(char), picidsize, texfile);
        printf("finished reading picture ID\n");
    } else {
        printf("skipping picture ID(not present)\n");
    }
    if(paltype > 0){
        printf("reading color palette\n");
        fseek(texfile, palstart, SEEK_CUR);
        fread(colorpalette, colorsize, pallength - palstart, texfile);
        printf("finished reading color palette\n");
    } else {
        printf("skipping color palette(not present)\n");
    }
    if(format == 0){
        printf("skipping pixel data\n");
    }else{
        if(compressed == true){
            for(int row = 0; row < picheight; row++){
                int curcol = 0;
                int totalindex = 0;
                uint8_t controlbyte = 0;
                uint32_t pixel = 0;
                while(curcol < picwidth){
                    controlbyte = fgetc(texfile);
                    if((controlbyte & 0b00000001) == 0){
                        fread(&(pixeldata[totalindex]), bpp / 8,
                                (controlbyte >> 1), texfile );
                        curcol += (controlbyte >> 1);
                        totalindex += (controlbyte >> 1);
                    } else {
                        fread( &pixel, bpp / 8, 1, texfile );
                        for( uint8_t i = 0; i < (controlbyte >> 1); i++ ){
                            pixeldata[totalindex] = pixel;
                        }
                        curcol += (controlbyte >> 1) + 1;
                        totalindex += (controlbyte >> 1) + 1;
                    }
                }
            }
        } else {
            switch(bpp){
            case 1:
                uint8_t *pixelbuffer = malloc(picwidth * picheight / 8 *
                        sizeof(uint8_t));
                fread(pixelbuffer, sizeof(uint8_t), picwidth * picheight / 8,
                        texfile);
                uint32_t white = 0b11111111111111111111111111111111;
                uint32_t black = 0b00000000000000000000000011111111;
                uint8_t buffer;
                for(int i = 0; i < picwidth * picheight; i++){
                    if((( pixelbuffer[( i / 8 ) + ( i % 8 )] >> i % 8 )
                            & 0b00000001) == 1){
                        pixeldata[i] = white;
                    } else {
                        pixeldata[i] = black;
                    }
                }
                break;
            case 8:
                printf("not yet implemented\n");
                break;
            case 15:
                uint16_t *pixelbuffer = malloc(picwidth * picheight *
                        sizeof(uint16_t));
                uint32_t buffer;
                for(int i = 0; i < picwidth * picheight; i++){
                    uint8_t red = pixelbuffer[i] & 0b0000000000111110;
                    uint8_t green;
                    uint8_t blue;
                    uint8_t alpha = pixel;
                    &buffer = 
            case 16:
            fread(pixeldata, sizeof(uint32_t), picwidth * picheight
    }
    if(zerov == 0){
        printf("reverting line order\n");
        for(int i = 0; i < picheight / 2; i++){
            uint32_t buffer[picwidth];
            memcpy(buffer, pixeldata[i * picwidth], picwidth*sizeof(uint32_t));
        if(zeroh == 0){
            printf("reverting lines\n");
            
        }
    } else {
        printf("keeping line order\n");
    }
    jbyteArray ret = (*env)->NewByteArray(env, picwidth * picheight * 4);
    jbyte buffer = 1;
    (*env)->SetByteArrayRegion(env, ret, 0, picwidth * picheight,
            (jbyte *)pixeldata );
    free(pixeldata);
    return ret;
}

