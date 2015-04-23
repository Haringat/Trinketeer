// include the c stubs of the exported native method
#include "com_ichmed_trinketeers_savefile_DataLoader.h"
#include "targaloader.h"
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>

JNIEXPORT jbyteArray JNICALL
        Java_com_ichmed_trinketeers_savefile_DataLoader_loadTextureFile
        (JNIEnv *env, jclass thisclass, jstring path){
    printf("entered native textureloader\n");
    jsize pathlen = (*env)->GetStringUTFLength(env, path);
    printf("The path is %u characters long\n", (int) pathlen);
    char *cpath;
    jboolean iscopy;
    cpath = (char *) ((*env)->GetStringUTFChars(env, path, &iscopy));
    cpath[pathlen] = '\0';
    targafile tga = {0,0,0,0,0,0};
    loadTextureData(cpath, &tga);
    printf("loaded %u pixels\n", tga.picwidth * tga.picheight);
    printf("releasing temporary resources\n");
    (*env)->ReleaseStringUTFChars(env, path, cpath);
    printf("released temporary resources\n");
    jbyteArray ret = (*env)->NewByteArray(env, 4 * tga.picwidth * tga.picheight);
    (*env)->SetByteArrayRegion(env, ret, 0, (4 * tga.picwidth * tga.picheight),
            (jbyte *)(tga.buffer) );
    printf("debug\n");
    if(tga.buffer > 0){
        free(tga.buffer);
    }
    return ret;
}

