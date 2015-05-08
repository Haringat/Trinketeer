#include "com_ichmed_trinketeers_util_KTXTexture.h"
#include <stdbool.h>
#include <GL/gl.h>
#define KTX_OPENGL 1
#include <ktx.h>

JNIEXPORT void JNICALL Java_com_ichmed_trinketeers_util_KTXTexture_load
  (JNIEnv *env, jobject this, jstring jpath){
    const char *path;
    jboolean *cp = 0;
    path = (*env)->GetStringUTFChars(env, jpath, cp);
    printf("Loading texture from %s\n", path);
    jfieldID texid_fid = 0;
    printf("debug\n");
    jclass thisclass = (*env)->FindClass(env, 
            "com/ichmed/trinketeers/util/KTXTexture");
    if(thisclass == 0){
        printf("ERROR:class not found\n");
        return;
    } else {
        printf("INFO:class found\n");
    }
    texid_fid = (*env)->GetFieldID(env, thisclass, "id", "I");
    /*texid_fid = (*env)->GetFieldID(env, (*env)->FindClass( env, 
            "com.ichmed.trinketeers.util.KTXTexture" ), "id", "I");*/
    if(texid_fid == 0){
        printf("ERROR:field not found\n");
        return;
    }else{
        printf("INFO:field found\n");
    }
    GLuint texid = (*env)->GetIntField(env, this, texid_fid);
    if(texid == 0){
        printf("ERROR:texid could not be retrieved.\n");
    }else{
        printf("INFO:texid found\n");
    }
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
        return;
    }case KTX_FILE_OPEN_FAILED:{
        printf("ERROR:KTX_FILE_OPEN_FAILED:the texture file could not be opened.\n");
        return;
    }case KTX_INVALID_OPERATION:{
        printf("ERROR:KTX_INVALID_OPERATION:invalid parameters specified.\n");
        return;
    }case KTX_UNEXPECTED_END_OF_FILE:{
        printf("ERROR:KTX_UNEXPECTED_END_OF_FILE:unexpected end of file(corrupted texture file?)\n");
        return;
    }case KTX_OUT_OF_MEMORY:{
        printf("ERROR:KTX_OUT_OF_MEMORY:out of memory\n");
        return;
    }case KTX_GL_ERROR:{
        printf("ERROR:CODE %d\n", err);
        return;
    }case KTX_SUCCESS:{
        printf("INFO:KTX_SUCCESS:texture loaded successfully.\n");
        break;
    }default:{
        printf("ERROR:ktxLoadTextureN:unknown error occured.\n");
        return;
    }
    }
    texid_fid = (*env)->GetFieldID(env, thisclass, "id", "I");
    if(mipmapped == true){
        printf("INFO:mipmap found.\n");
    }else{
        printf("INFO:no mipmap present.\n");
    }
    if(texid_fid == 0){
        printf("ERROR:field not found\n");
        return;
    }else{
        printf("INFO:field found\n");
    }
    printf("INFO:the loaded texture is %ux%ux%u\n", dim.width, dim.height, dim.depth);
    printf("INFO:found %d meta fields.\n", pkvdlen);
    printf("INFO:TEXID:%d\n", texid);
    for(int i = 0; i < pkvdlen; i++){
        printf("%s\n", ppkvd[i]);
    }
    return;
}
