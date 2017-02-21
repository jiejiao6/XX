/* AXML Parser
 * https://github.com/claudxiao/AndTools
 * Claud Xiao <iClaudXiao@gmail.com>
 */
#ifndef AXMLPARSER_H
#define AXMLPARSER_H

#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>


extern uint32_t g_styleDataOff;   //style���ݿ����ʼ��ַ
extern uint32_t g_appTag_nameOff;  //����application tag�е�tagname��Ա���ļ�ͷ��ƫ��ֵ��tagname��Ա��ֵΪһ���ַ���������
                            //���ǿ��Ը��ݸ�ƫ��ֵ����attribution���������޸Ĳ�����

extern uint32_t g_curStringCount; //���浱ǰstringChunk�ܹ����е�string����
extern uint32_t g_appURIindex; //����application���������ռ��uri����ֵ
extern uint32_t g_res_ChunkSizeOffset; //����resourcesChunk��chunksizeƫ��ֵ


typedef enum{
	AE_STARTDOC = 0,
	AE_ENDDOC,
	AE_STARTTAG,
	AE_ENDTAG,
	AE_TEXT,
	AE_ERROR,
} AxmlEvent_t;


#ifdef __cplusplus
#if __cplusplus
extern "C" {
#endif
#endif

void *AxmlOpen(char *buffer, size_t size);

AxmlEvent_t AxmlNext(void *axml);

char *AxmlGetTagPrefix(void *axml);
char *AxmlGetTagName(void *axml);

int AxmlNewNamespace(void *axml);
char *AxmlGetNsPrefix(void *axml);
char *AxmlGetNsUri(void *axml);

uint32_t AxmlGetAttrCount(void *axml);
char *AxmlGetAttrPrefix(void *axml, uint32_t i);
char *AxmlGetAttrName(void *axml, uint32_t i);
char *AxmlGetAttrValue(void *axml, uint32_t i);

char *AxmlGetText(void *axml);

int AxmlClose(void *axml);

int AxmlToXml(char **outbuf, size_t *outsize, char *inbuf, size_t insize);

#ifdef __cplusplus
#if __cplusplus
};
#endif
#endif

#endif
