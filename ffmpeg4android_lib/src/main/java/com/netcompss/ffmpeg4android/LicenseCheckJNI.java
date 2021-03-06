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
 */
package com.netcompss.ffmpeg4android;

import android.content.Context;


public class LicenseCheckJNI
{

    public int licenseCheck(String path, Context ctx) {
    	String rcStr = "-100";
   		rcStr = licenseCheckComplexJNI(path);
    	int rc =Integer.decode(rcStr);
    	return rc;
    }

   
    public native String licenseCheckComplexJNI(String path);
    public native String licenseCheckSimpleJNI(String path);

    

    
    static {
        System.loadLibrary("license-jni");
    }
}
