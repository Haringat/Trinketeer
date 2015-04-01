// include the c stubs of the exported native method
#include "com_ichmed_trinketeers_savefile_DataLoader.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

JNIEXPORT jbyteArray JNICALL Java_com_ichmed_trinketeers_savefile_DataLoader_loadTextureFile(JNIEnv *env, jclass thisclass, jstring path){
    jsize pathlen = (*env)->GetStringUTFLength(env, path);
    printf("%d\n", pathlen);
    char *cpath = malloc((pathlen+1) * sizeof(char));
    jboolean iscopy;
    cpath = (char *) (*env)->GetStringUTFChars(env, path, &iscopy);
    cpath[pathlen] = '\0';
    printf("loading texture from %s\n", cpath);
    FILE *texfile = fopen(cpath, "r");
    (*env)->ReleaseStringUTFChars(env, path, cpath);
    char head[18];
    fread(&head, 18, 1, texfile);
    int picidsize               = head[0];
    int paltype                 = head[1];
    int pictype                 = head[2];
    int palstart                = head[3];
    *(((char *)(&palstart))+1)  = head[4];
    int pallength               = head[5];
    *(((char *)(&pallength))+1) = head[6];
    int colorsize               = head[7];
    int zerox                   = head[8];
    *(((char *)(&zerox))+1)     = head[9];
    int zeroy                   = head[10];
    *(((char *)(&zeroy))+1)     = head[11];
    int picwidth                = head[12];
    *(((char *)(&picwidth))+1)  = head[13];
    int picheight               = head[14];
    *(((char *)(&picheight))+1) = head[15];
    int bpp                     = head[16];
    int attributes              = head[17];
    int attbpp                  = (attributes & 0b11110000) >> 4;
    int zeroh                   = (attributes & 0b00001000) >> 3;
    int zerov                   = (attributes & 0b00000100) >> 2;
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
        return NewByteArray(env, 0);
    }
    int compressed = pictype & 0b1000;
    int format = pictype & 0b11;
    if(format == 0){
        printf("no picture data detected(this is not an error)\n");
    } else {
        if(compressed > 0){
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
    printf("the origin is in the %s %s corner\n", (zerov == 1) ? "upper" : "lower", (zeroh == 1) ? "right" : "left");
    char picid[picidsize];
    char colorpalette[pallength - palstart];
    char pixeldata[picwidth*picheight*bpp/8];
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
        if(compressed > 0){
            for(int row = 0; row < picheight; row++){
                int curcol = 0;
                char pixel[1+bpp];
                while(curcol < picwidth){
                    fread(pixel, 1+(bpp/8), 1, texfile);
                    for(int i = 0; i <= pixel[0] >> 1; i++){
                        memcpy(pixeldata, *(pixel[1]), bpp/8);
                    }
                    curcol+= (pixel[0] >> 1) + 1;
                }
            }
        } else {
            fread(pixeldata, sizeof(char), picwidth*picheight*bpp/8, texdata);
            pixeldata = malloc(picwidth*picheight*bpp/8 * sizeof(char));
            memcpy(pixeldata, pixeldata, picwidth*picheight*bpp/8);
        }
    }
    jbyteArray ret = (*env)->NewByteArray(env, 1);
    jbyte buffer = 1;
    (*env)->SetByteArrayRegion(env, ret, 0, 1, &buffer );
    return ret;
}

