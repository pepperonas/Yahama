/*
 * Copyright (c) 2019 Martin Pfeffer
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

package com.pepperonas.yahama.app.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class RootFile {

    private final String file;


    public RootFile(String file) {
        this.file = file;
    }


    public String getName() {
        return RootUtilities.runCommand("basename '" + file + "'");
    }


    public void mkdir() {
        RootUtilities.runCommand("mkdir -p '" + file + "'");
    }


    public void mv(String newPath) {
        RootUtilities.runCommand("mv -f '" + file + "' '" + newPath + "'");
    }


    public void write(String text, boolean append) {
        RootUtilities.runCommand(append ? "echo '" + text + "' >> " + file : "echo '" + text + "' > " + file);
    }


    public void delete() {
        RootUtilities.runCommand("rm -r '" + file + "'");
    }


    public List<String> list() {
        List<String> list = new ArrayList<String>();
        String files = RootUtilities.runCommand("ls '" + file + "'");
        if (files != null)
            // Make sure the file exists
            for (String file : files.split("\\r?\\n"))
                if (file != null && !file.isEmpty() && Utils.existFile(this.file + "/" + file))
                    list.add(file);
        return list;
    }


    public boolean isEmpty() {
        return RootUtilities.runCommand("find '" + file + "' -mindepth 1 | read || echo false").equals("false");
    }


    public boolean exists() {
        String output = RootUtilities.runCommand("[ -e '" + file + "' ] && echo true");
        return output != null && output.contains("true");
    }


    public String readFile() {
        return RootUtilities.runCommand("cat '" + file + "'");
    }


    public String toString() {
        return file;
    }

}