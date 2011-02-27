/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/vfs.h>

 
#include <string.h>
#include <jni.h>

/*
 * Functions
 */
JNIEXPORT jint JNICALL 
Java_net_samdroid_samdroidtools_SysCommand_runSh(JNIEnv *env, jclass class)
{
	if (chmod("/data/data/net.samdroid.samdroidtools/samdroidcmd.sh", 0755) < 0) return (jint)(-1);
	return (jint)system("su -c \"/data/data/net.samdroid.samdroidtools/samdroidcmd.sh\"");
}

JNIEXPORT jint JNICALL 
Java_net_samdroid_samdroidtools_SamdroidTools_getFreeSpace(JNIEnv *env, jclass class, jstring js_path)
{
	struct statfs fiData;
	const char *s_path;
	int exitcode;
	s_path = (*env)->GetStringUTFChars(env, js_path, 0);
        if((statfs(s_path, &fiData)) < 0 ) {
		exitcode = -1;
	}
	else {
		exitcode = fiData.f_bavail*fiData.f_bsize;
	}
	(*env)->ReleaseStringUTFChars(env, js_path, s_path);  
	return (jint)exitcode;
}

/*
JNIEXPORT jint JNICALL 
Java_net_samdroid_samdroidtools_SamdroidTools_runCmd(JNIEnv *env, jclass class, jstring command)
{
	const char *commandString;
	commandString = (*env)->GetStringUTFChars(env, command, 0);
	int exitcode = system(commandString); 
	(*env)->ReleaseStringUTFChars(env, command, commandString);  
	return (jint)exitcode;
}


JNIEXPORT jint JNICALL 
Java_net_samdroid_samdroidtools_SamdroidTools_reboot(JNIEnv *env, jclass class, jint type)
{
	int exitcode;
	system("su -c sync");
	if ((int)type == 0) {
	    exitcode = system("su -c reboot"); 
	} 
 	else if ((int)type == 1) {
	    exitcode = system("su -c \"reboot recovery\""); 
	}
	return (jint)exitcode;
}
*/

/*
jstring
Java_com_example_hellojni_HelloJni_stringFromJNI( JNIEnv* env,
                                                  jobject thiz )
{
	int depth = 200;

	jclass cls = (*env)->GetObjectClass(env, thiz);
	jmethodID mid = (*env)->GetStaticMethodID(env, cls, "UpdateV", "(ILjava/lang/String;)I");
	if (mid == 0)
		return (*env)->NewStringUTF(env, "Error -> ");
	depth = (*env)->CallStaticIntMethod(env, cls, mid, depth, (*env)->NewStringUTF(env, "Oops - string.... :)"));


    return (*env)->NewStringUTF(env, "Hello from JNI ! LeshaK (c) 2009 -> ");
}
*/
