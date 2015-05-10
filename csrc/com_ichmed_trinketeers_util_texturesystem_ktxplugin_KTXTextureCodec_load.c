#include "com_ichmed_trinketeers_util_texturesystem_ktxplugin_KTXTextureCodec.h"
#include <stdbool.h>
#include <GL/gl.h>
#define KTX_OPENGL 1
#include <ktx.h>

JNIEXPORT jobject JNICALL Java_com_ichmed_trinketeers_util_texturesystem_ktxplugin_KTXTextureCodec_load
  (JNIEnv *env, jclass thisclass, jstring jpath){
    const char *path;
    jboolean *cp = 0;
    path = (*env)->GetStringUTFChars(env, jpath, cp);
    printf("Loading texture from %s\n", path);
    printf("debug\n");
    jclass texclass = (*env)->FindClass(env,
            "com/ichmed/trinketeers/util/texturesystem/ktxplugin/KTXTexture");
    if(thisclass == 0){
        printf("ERROR:texture class not found\n");
        return 0;
    } else {
        printf("INFO:texture class found\n");
    }
    GLuint texid = 0;
    KTX_dimensions dim = {0,0,0};
    GLboolean mipmapped;
    GLenum err;
    GLenum pTarget;
    unsigned int pkvdlen = 0;
    unsigned char **ppkvd = 0;
    KTX_error_code retval = ktxLoadTextureN(path, &texid, &pTarget,
            &dim, &mipmapped, &err, &pkvdlen, ppkvd);
    switch(retval){
    case KTX_INVALID_VALUE:{
        printf("ERROR:KTX_INVALID_VALUE:invalid parameters specified.\n");
        return 0;
    }case KTX_FILE_OPEN_FAILED:{
        printf("ERROR:KTX_FILE_OPEN_FAILED:the texture file could not be opened.\n");
        return 0;
    }case KTX_INVALID_OPERATION:{
        printf("ERROR:KTX_INVALID_OPERATION:invalid parameters specified.\n");
        return 0;
    }case KTX_UNEXPECTED_END_OF_FILE:{
        printf("ERROR:KTX_UNEXPECTED_END_OF_FILE:unexpected end of file(corrupted texture file?)\n");
        return 0;
    }case KTX_OUT_OF_MEMORY:{
        printf("ERROR:KTX_OUT_OF_MEMORY:out of memory\n");
        return 0;
    }case KTX_GL_ERROR:{
        printf("ERROR:CODE %d\n", err);
        return 0;
    }case KTX_SUCCESS:{
        printf("INFO:KTX_SUCCESS:texture loaded successfully.\n");
        break;
    }default:{
        printf("ERROR:ktxLoadTextureN:unknown error occured.\n");
        return 0;
    }
    }
    printf("INFO:GL gave us the texture id %d\n", texid);
    jobject tex = (*env)->NewObject(env,texclass, (*env)->GetMethodID(env, texclass,
            "<init>", "(II)V"), texid, pTarget);
    if(mipmapped == true){
        printf("INFO:mipmap found.\n");
    }else{
        printf("INFO:no mipmap present.\n");
    }
    printf("INFO:the loaded texture is %ux%ux%u\n", dim.width, dim.height, dim.depth);
    printf("INFO:found %d meta fields.\n", pkvdlen);
    printf("INFO:TEXID:%d\n", texid);
    for(int i = 0; i < pkvdlen; i++){
        printf("%s\n", ppkvd[i]);
    }
    return tex;
}
