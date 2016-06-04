/*
 * Copyright (c) 2016 Martin Pfeffer
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

package com.pepperonas.yahama.app.utility;

import com.pepperonas.yahama.app.model.AmpYaRxV577;
import com.pepperonas.yahama.app.utils.Utils;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class Commands {

    private static final String TAG = "Commands";

    //  @formatter:off

    private static final String
                 XML_HEAD = "<?xml version=\"1.0\" encoding=\"utf-8\"?>",
        YAMAHA_AV_CMD_GET = "<YAMAHA_AV cmd=\"GET\">",
        YAMAHA_AV_CMD_PUT = "<YAMAHA_AV cmd=\"PUT\">",
        YAMAHA_AV_CLOSING = "</YAMAHA_AV>";


    private static final String RESET = "<Reset>Execute</Reset>";


    public static final String GET_INFO =
            XML_HEAD +
            YAMAHA_AV_CMD_GET +
                "<Main_Zone>" +
                        "<Basic_Status>GetParam</Basic_Status>" +
                "</Main_Zone>" +
            YAMAHA_AV_CLOSING;



    public static String GET_DSP_INFO(String which) {

        boolean isCategory = Utils.stringEquals(
                which,
                AmpYaRxV577.CAT_CLASSICAL, AmpYaRxV577.CAT_LIVE_CLUB,
                AmpYaRxV577.CAT_MOVIE, AmpYaRxV577.CAT_ENTERTAINMENT);

        if (isCategory) {
            return XML_HEAD +
                   YAMAHA_AV_CMD_GET +
                       "<Main_Zone>" +
                           "<Surround>" +
                               "<Sound_Program_Param>" +
                                    "<" + which + ">" + "GetParam" + "</" + which + ">" +
                               "</Sound_Program_Param>" +
                           "</Surround>" +
                       "</Main_Zone>" +
                   "</YAMAHA_AV>";
        } else {
            which = AmpYaRxV577.getCategory(which);
            return XML_HEAD +
                   YAMAHA_AV_CMD_GET +
                       "<Main_Zone>" +
                           "<Surround>" +
                               "<Sound_Program_Param>" +
                                    "<" + which + ">" + "GetParam" + "</" + which + ">" +
                               "</Sound_Program_Param>" +
                           "</Surround>" +
                       "</Main_Zone>" +
                   "</YAMAHA_AV>";
        }
    }



    /**
     * @param volume abs. value (!10)
     */
    public static String SET_VOL(int volume) {
        return XML_HEAD +
               YAMAHA_AV_CMD_PUT +
                   "<Main_Zone>" +
                       "<Volume>" +
                           "<Lvl>" +
                                "<Val>" + String.valueOf(volume) + "</Val>" +
                                "<Exp>1</Exp><Unit>dB</Unit>" +
                           "</Lvl>" +
                       "</Volume>" +
                   "</Main_Zone>" +
               YAMAHA_AV_CLOSING;
    }



    public static String SET_BASS_OR_TREBLE(String which, int volume) {
        return XML_HEAD +
               YAMAHA_AV_CMD_PUT +
                   "<Main_Zone>" +
                       "<Sound_Video>" +
                           "<Tone>" +
                               "<" + which + ">" +
                                   "<Val>" + String.valueOf(volume) + "</Val>" +
                                   "<Exp>1</Exp><Unit>dB</Unit>" +
                               "</" + which + ">" +
                           "</Tone>" +
                       "</Sound_Video>" +
                   "</Main_Zone>" +
               YAMAHA_AV_CLOSING;
    }



    /**
     * @param state "On", "Off"
     */
    public static String TOGGLE_MUTE(String state) {
        return XML_HEAD +
               YAMAHA_AV_CMD_PUT +
                   "<Main_Zone>" +
                       "<Volume>" +
                            "<Mute>" + state + "</Mute>" +
                       "</Volume>" +
                   "</Main_Zone>" +
               YAMAHA_AV_CLOSING;
    }



    /**
     * @param state "On", "Auto", "Off"
     */
    public static String SET_DIRECT(String state) {
        return XML_HEAD +
               YAMAHA_AV_CMD_PUT +
                   "<Main_Zone>" +
                       "<Sound_Video>" +
                           "<Direct>" +
                                "<Mode>" + state + "</Mode>" +
                           "</Direct>" +
                       "</Sound_Video>" +
                   "</Main_Zone>" +
               YAMAHA_AV_CLOSING;
    }



    public static String SET_DRC(String which, String state) {
        return XML_HEAD +
               YAMAHA_AV_CMD_PUT +
                   "<Main_Zone>" +
                       "<Surround>" +
                            "<" + which + ">" + state + "</" + which + ">" +
                       "</Surround>" +
                   "</Main_Zone>" +
               YAMAHA_AV_CLOSING;
    }



    public static String SET_ADAPTIVE_DRC(String which, String state) {
        return XML_HEAD +
               YAMAHA_AV_CMD_PUT +
                   "<Main_Zone>" +
                       "<Sound_Video>" +
                            "<" + which + ">" + state + "</" + which + ">" +
                       "</Sound_Video>" +
                   "</Main_Zone>" +
               YAMAHA_AV_CLOSING;
    }



    public static String SET_EXTRA_BASS(String which, String state) {
        return XML_HEAD +
               YAMAHA_AV_CMD_PUT +
                   "<Main_Zone>" +
                       "<Sound_Video>" +
                            "<" + which + ">" + state + "</" + which + ">" +
                       "</Sound_Video>" +
                   "</Main_Zone>" +
               YAMAHA_AV_CLOSING;
    }



    public static String SET_ENHANCER(String which, String state) {
        return XML_HEAD +
               YAMAHA_AV_CMD_PUT +
                   "<Main_Zone>" +
                       "<Surround>" +
                           "<Program_Sel>" +
                               "<Current>" +
                                    "<" + which + ">" + state + "</" + which + ">" +
                               "</Current>" +
                           "</Program_Sel>" +
                       "</Surround>" +
                   "</Main_Zone>" +
               YAMAHA_AV_CLOSING;
    }



    /**
     * @param input "AV1", "AV2", "AV3", "AV4",
     *              "DVD", "Spotify", "NET_RADIO", "TUNER",
     *              "USB", "AUX", "AUDIO",
     *              "HDMI1", "HDMI2", "HDMI3", "HDMI4"
     */
    public static String SELECT_INPUT(String input) {
        return XML_HEAD +
               YAMAHA_AV_CMD_PUT +
                   "<Main_Zone>" +
                       "<Input>" +
                            "<Input_Sel>" + input + "</Input_Sel>" +
                       "</Input>" +
                   "</Main_Zone>" +
               YAMAHA_AV_CLOSING;
    }



    /**
     * @param dsp -> Amplifier DSPs...
     */
    public static String SET_DSP(String dsp) {
        return XML_HEAD +
               YAMAHA_AV_CMD_PUT +
                   "<Main_Zone>" +
                       "<Surround>" +
                           "<Program_Sel>" +
                               "<Current>" +
                                    "<Sound_Program>" + dsp + "</Sound_Program>" +
                               "</Current>" +
                           "</Program_Sel>" +
                       "</Surround>" +
                   "</Main_Zone>" +
               YAMAHA_AV_CLOSING;
    }



    /**
     * @param state -> "On", "Off"
     */
    public static String SET_DSP_STRAIGHT(String state) {
        return XML_HEAD +
               YAMAHA_AV_CMD_PUT +
                   "<Main_Zone>" +
                       "<Surround>" +
                           "<Program_Sel>" +
                               "<Current>" +
                                    "<Straight>" + state + "</Straight>" +
                               "</Current>" +
                           "</Program_Sel>" +
                       "</Surround>" +
                   "</Main_Zone>" +
               YAMAHA_AV_CLOSING;
    }



    /**
     * @param state "On", "Standby"
     */
    public static String SET_POWER(String state) {
        return XML_HEAD +
               YAMAHA_AV_CMD_PUT +
                   "<Main_Zone>" +
                       "<Power_Control>" +
                            "<Power>" + state + "</Power>" +
                       "</Power_Control>" +
                   "</Main_Zone>" +
               YAMAHA_AV_CLOSING;
    }



    /**
     * @param action "Play", "Pause", "Stop"
     */
    public static String PLAYBACK_CONTROL(String action) {
        return XML_HEAD +
               YAMAHA_AV_CMD_PUT +
                   "<Main_Zone>" +
                       "<Play_Control>" +
                            "<Playback>" + action + "</Playback>" +
                       "</Play_Control>" +
                   "</Main_Zone>" +
               YAMAHA_AV_CLOSING;
    }



    /**
     * @param mins 0, 30, 60, 90, 120
     */
    public static String SET_SLEEPTIMER(int mins) {
        return XML_HEAD +
               YAMAHA_AV_CMD_PUT +
                   "<Main_Zone>" +
                       "<Power_Control>" +
                            "<Sleep>" + (mins == 0 ? "Off" : String.valueOf(mins) + " min") + "</Sleep>" +
                       "</Power_Control>" +
                   "</Main_Zone>" +
               YAMAHA_AV_CLOSING;
    }



    public static String SPEAKER_CONFIG(boolean spkrA, boolean spkrB){
        return XML_HEAD +
               YAMAHA_AV_CMD_PUT +
                   "<Main_Zone>" +
                       "<Speaker_Preout>" +
                           "<Speaker_AB>" +
                               "<Speaker_A>" + (spkrA? "On" : "Off") + "</Speaker_A>" +
                               "<Speaker_B>" + (spkrB? "On" : "Off") + "</Speaker_B>" +
                           "</Speaker_AB>" +
                       "</Speaker_Preout>" +
                   "</Main_Zone>" +
               YAMAHA_AV_CLOSING;
    }



    public static String RESET_SURROUND_3D_CONFIG(){

        return XML_HEAD +
               YAMAHA_AV_CMD_PUT +
                    "<Main_Zone>" +
                        "<Surround>" +
                            "<Sound_Program_Param>" +
                                "<STEREO>" +
                                    "<Xch>" +
                                        RESET +
                                    "</Xch>" +
                                "</STEREO>" +
                            "</Sound_Program_Param>" +
                        "</Surround>" +
                    "</Main_Zone>" +
                YAMAHA_AV_CLOSING;
    }



    public static String SURROUND_DSP_3D_CONFIG(int leftRight, int frontRear){

        int level = 0;
        int heightBalance = 5;

        return XML_HEAD +
               YAMAHA_AV_CMD_PUT +
                    "<Main_Zone>" +
                        "<Surround>" +
                            "<Sound_Program_Param>" +
                                "<STEREO>" +
                                    "<Xch>" +

                                        "<Lvl>" +
                                            "<Val>" + String.valueOf(level) + "</Val>" +
                                            "<Exp>0</Exp><Unit></Unit>" +
                                        "</Lvl>" +

                                        "<Front_Rear_Balance>" +
                                            "<Val>" + String.valueOf(frontRear) + "</Val>" +
                                            "<Exp>0</Exp><Unit></Unit>" +
                                        "</Front_Rear_Balance>" +

                                        "<Left_Right_Balance>" +
                                            "<Val>" + String.valueOf(leftRight) + "</Val>" +
                                            "<Exp>0</Exp><Unit></Unit>" +
                                        "</Left_Right_Balance>" +

                                        "<Height_Balance>" +
                                            "<Val>" + String.valueOf(heightBalance) +"</Val>" +
                                            "<Exp>0</Exp><Unit></Unit>" +
                                        "</Height_Balance>" +

                                    "</Xch>" +
                                "</STEREO>" +
                            "</Sound_Program_Param>" +
                        "</Surround>" +
                    "</Main_Zone>" +
                YAMAHA_AV_CLOSING;
    }



    /**
     * @param which: "Hall_in_Munich", "Hall_in_Vienna", "Cellar_Club", "The_Bottom_Line"
     *
     * @param dspLvl:   min. -6 | max. 3
     * @param initDly:  min. 1  | max. 99
     * @param roomSize: min. 1  | max. 20
     * @param liveness: min. 0  | max. 10
     * */
    public static String SET_3D_DSP_MUSIC (String which,
                                           int dspLvl, int initDly,
                                           int roomSize, int liveness) {

        String category;
        if(which.equals(AmpYaRxV577.XML_CELLAR_CLUB)
                          || which.equals(AmpYaRxV577.XML_THE_BOTTOM_LINE)) {
            category = AmpYaRxV577.CAT_LIVE_CLUB;
        } else category = AmpYaRxV577.CAT_CLASSICAL;

        dspLvl = Utils.setMinMaxWhenOutOfRange(-6, 3, dspLvl);
        initDly = Utils.setMinMaxWhenOutOfRange(1, 99, initDly);
        roomSize = Utils.setMinMaxWhenOutOfRange(1, 20, roomSize);
        liveness = Utils.setMinMaxWhenOutOfRange(0, 10, liveness);

        return XML_HEAD +
               YAMAHA_AV_CMD_PUT + 
                    "<Main_Zone>" +
                        "<Surround>" +
                            "<Sound_Program_Param>" +

                                "<" + category + ">" +
                                    "<" + which + ">" +

                                        "<DSP_Lvl>" +
                                            "<Val>" + String.valueOf(dspLvl) + "</Val>" +
                                            "<Exp>0</Exp><Unit>dB</Unit>" +
                                        "</DSP_Lvl>" +

                                        "<Init_Dly>" +
                                            "<Val>" + String.valueOf(initDly) + "</Val>" +
                                            "<Exp>0</Exp><Unit>ms</Unit>" +
                                        "</Init_Dly>" +

                                        "<Room_Size>" +
                                            "<Val>" + String.valueOf(roomSize) + "</Val>" +
                                            "<Exp>1</Exp><Unit/>" +
                                        "</Room_Size>" +

                                        "<Liveness>" +
                                            "<Val>" + String.valueOf(liveness) + "</Val>" +
                                            "<Exp>0</Exp><Unit/>" +
                                        "</Liveness>" +

                                    "</" + which + ">" +
                                "</" + category + ">" +

                            "</Sound_Program_Param>" +
                        "</Surround>" +
                    "</Main_Zone>" +
                YAMAHA_AV_CLOSING;
    }



    /**
     * @param which: "The_Roxy_Theatre", "Mono_Movie", "Chamber"
     *
     * @param dspLvl:   min. -6 | max. 3
     * @param initDly:  min.  1 | max. 99
     * @param roomSize: min.  1 | max. 20
     * @param liveness: min.  0 | max. 10
     * @param revTime:  min. 10 | max. 50
     * @param revDly:   min.  0 | max. 250
     * @param revLvl:   min.  0 | max. 100
     * */
    public static String SET_3D_DSP_MUSIC_ADVANCED (String which,
                                                    int dspLvl, int initDly,
                                                    int roomSize, int liveness,
                                                    int revTime, int revDly, int revLvl) {

        String category = "";
        if (which.equals(AmpYaRxV577.CHAMBER)) {
            category = AmpYaRxV577.CAT_CLASSICAL;
        } else if (which.equals(AmpYaRxV577.XML_THE_ROXY_THEATRE)) {
            category = AmpYaRxV577.CAT_LIVE_CLUB;
        } else if (which.equals(AmpYaRxV577.XML_MONO_MOVIE)) {
            category = AmpYaRxV577.CAT_MOVIE;
        }

        dspLvl = Utils.setMinMaxWhenOutOfRange(-6, 3, dspLvl);
        initDly = Utils.setMinMaxWhenOutOfRange(1, 99, initDly);
        roomSize = Utils.setMinMaxWhenOutOfRange(1, 20, roomSize);
        liveness = Utils.setMinMaxWhenOutOfRange(0, 10, liveness);
        revTime = Utils.setMinMaxWhenOutOfRange(10, 50, revTime);
        revDly = Utils.setMinMaxWhenOutOfRange(0, 250, revDly);
        revLvl = Utils.setMinMaxWhenOutOfRange(0, 100, revLvl);

        return XML_HEAD +
               YAMAHA_AV_CMD_PUT +
                    "<Main_Zone>" +
                        "<Surround>" +
                            "<Sound_Program_Param>" +

                                "<" + category + ">" +
                                    "<" + which + ">" +

                                        "<DSP_Lvl>" +
                                            "<Val>" + String.valueOf(dspLvl) + "</Val>" +
                                            "<Exp>0</Exp><Unit>dB</Unit>" +
                                        "</DSP_Lvl>" +

                                        "<Init_Dly>" +
                                            "<Val>" + String.valueOf(initDly) + "</Val>" +
                                            "<Exp>0</Exp><Unit>ms</Unit>" +
                                        "</Init_Dly>" +

                                        // no room-size for chamber
                                        (which.equals(AmpYaRxV577.CHAMBER) ? "" :
                                         "<Room_Size>" +
                                            "<Val>" + String.valueOf(roomSize) + "</Val>" +
                                            "<Exp>1</Exp><Unit/>" +
                                        "</Room_Size>") +

                                        "<Liveness>" +
                                            "<Val>" + String.valueOf(liveness) + "</Val>" +
                                            "<Exp>0</Exp><Unit/>" +
                                        "</Liveness>" +

                                        "<Rev_Time>" +
                                            "<Val>" + String.valueOf(revTime) + "</Val>" +
                                            "<Exp>1</Exp><Unit>s</Unit>" +
                                        "</Rev_Time>" +

                                        "<Rev_Dly>" +
                                            "<Val>" + String.valueOf(revDly) + "</Val>" +
                                            "<Exp>0</Exp><Unit>ms</Unit>" +
                                        "</Rev_Dly>" +

                                        "<Rev_Lvl>" +
                                            "<Val>" + String.valueOf(revLvl) + "</Val>" +
                                            "<Exp>0</Exp><Unit>%</Unit>" +
                                        "</Rev_Lvl>" +

                                    "</" + which + ">" +
                                "</" + category + ">" +

                            "</Sound_Program_Param>" +
                        "</Surround>" +
                    "</Main_Zone>" +
                YAMAHA_AV_CLOSING;
    }



    /**
     * @param which: "Action_Game" "Adventure", "Drama", "Music_Video", "Roleplaying_Game",
     *             "Sci_Fi", "Spectacle", "Sports"
     *
     * @param dspLvl:   min. -6 | max. 3
     * @param initDly:  min.  1 | max. 99
     * @param roomSize: min.  1 | max. 20
     *
     * @param surInitDly      min. 1 | max. 49
     * @param surRoomSize     min. 1 | max. 20
     * @param surBackInitDly  min. 1 | max. 49
     * @param surBackRoomSize min. 1 | max. 20
     * */
    public static String SET_3D_DSP_MOVIE (String which,
                                           int dspLvl, int initDly, int roomSize, int surInitDly,
                                           int surRoomSize, int surBackInitDly, int surBackRoomSize) {

        String category;
        if (which.equals(AmpYaRxV577.XML_MUSIC_VIDEO)
                || which.equals(AmpYaRxV577.XML_ROLEPLAYING_GAME)
                || which.equals(AmpYaRxV577.SPORTS)
                || which.equals(AmpYaRxV577.XML_ACTION_GAME)) {
            category = AmpYaRxV577.CAT_ENTERTAINMENT;
        } else {
            category = AmpYaRxV577.CAT_MOVIE;
        }

        dspLvl = Utils.setMinMaxWhenOutOfRange(-6, 3, dspLvl);
        initDly = Utils.setMinMaxWhenOutOfRange(1, 99, initDly);
        roomSize = Utils.setMinMaxWhenOutOfRange(1, 20, roomSize);
        surInitDly = Utils.setMinMaxWhenOutOfRange(1, 49, surInitDly);
        surRoomSize = Utils.setMinMaxWhenOutOfRange(1, 20, surRoomSize);
        surBackInitDly = Utils.setMinMaxWhenOutOfRange(1, 49, surBackInitDly);
        surBackRoomSize = Utils.setMinMaxWhenOutOfRange(1, 20, surBackRoomSize);

        return XML_HEAD +
               YAMAHA_AV_CMD_PUT +
                    "<Main_Zone>" +
                        "<Surround>" +
                            "<Sound_Program_Param>" +

                                "<" + category + ">" +
                                    "<" + which + ">" +

                                        "<DSP_Lvl>" +
                                            "<Val>" + String.valueOf(dspLvl) + "</Val>" +
                                            "<Exp>0</Exp><Unit>dB</Unit>" +
                                        "</DSP_Lvl>" +

                                        "<Init_Dly>" +
                                            "<Val>" + String.valueOf(initDly) + "</Val>" +
                                            "<Exp>0</Exp><Unit>ms</Unit>" +
                                        "</Init_Dly>" +

                                        "<Room_Size>" +
                                            "<Val>" + String.valueOf(roomSize) + "</Val>" +
                                            "<Exp>1</Exp><Unit/>" +
                                        "</Room_Size>" +

                                        "<Sur_Init_Dly>" +
                                            "<Val>" + String.valueOf(surInitDly) + "</Val>" +
                                            "<Exp>0</Exp><Unit>ms</Unit>" +
                                        "</Sur_Init_Dly>" +

                                        "<Sur_Room_Size>" +
                                            "<Val>" + String.valueOf(surRoomSize) + "</Val>" +
                                            "<Exp>1</Exp><Unit/>" +
                                        "</Sur_Room_Size>" +

                                        "<Sur_Back_Init_Dly>" +
                                            "<Val>" + String.valueOf(surBackInitDly) + "</Val>" +
                                            "<Exp>0</Exp><Unit>ms</Unit>" +
                                        "</Sur_Back_Init_Dly>" +

                                        "<Sur_Back_Room_Size>" +
                                            "<Val>" + String.valueOf(surBackRoomSize) + "</Val>" +
                                            "<Exp>1</Exp><Unit/>" +
                                        "</Sur_Back_Room_Size>" +

                                    "</" + which + ">" +
                                "</" + category + ">" +

                            "</Sound_Program_Param>" +
                        "</Surround>" +
                    "</Main_Zone>" +
                YAMAHA_AV_CLOSING;
    }



    /**
     * @param which: "Action_Game" "Adventure", "Drama", "Music_Video", "Roleplaying_Game",
     *             "Sci_Fi", "Spectacle", "Sports"
     * */
    public static String RESET_3D_DSP (String category, String which) {

        return XML_HEAD +
               YAMAHA_AV_CMD_PUT +
                    "<Main_Zone>" +
                        "<Surround>" +
                            "<Sound_Program_Param>" +

                                "<" + category + ">" +
                                    "<" + which + ">" +
                                            RESET +
                                    "</" + which + ">" +
                                "</" + category + ">" +

                            "</Sound_Program_Param>" +
                        "</Surround>" +
                    "</Main_Zone>" +
                YAMAHA_AV_CLOSING;
    }



    /**
     * @param dspLvl:       min. -6 | max. 3
     * @param surInitDly:   min.  1 | max. 49
     * @param surRoomSize:  min.  1 | max. 20
     * @param surLiveness:  min.  1 | max. 10
     *
     * @param surBackInitDly      min. 1 | max. 49
     * @param surBackRoomSize     min. 1 | max. 20
     * @param surBackLiveness     min. 1 | max. 10
     * */
    public static String SET_3D_DSP_STANDARD (int dspLvl, int surInitDly, int surRoomSize,
                                              int surLiveness, int surBackInitDly, int surBackRoomSize,
                                              int surBackLiveness) {

        dspLvl = Utils.setMinMaxWhenOutOfRange(-6, 3, dspLvl);
        surInitDly = Utils.setMinMaxWhenOutOfRange(1, 49, surInitDly);
        surRoomSize = Utils.setMinMaxWhenOutOfRange(1, 20, surRoomSize);
        surLiveness = Utils.setMinMaxWhenOutOfRange(1, 10, surLiveness);
        surBackInitDly = Utils.setMinMaxWhenOutOfRange(1, 49, surBackInitDly);
        surBackRoomSize = Utils.setMinMaxWhenOutOfRange(1, 20, surBackRoomSize);
        surBackLiveness = Utils.setMinMaxWhenOutOfRange(1, 10, surBackLiveness);

        return XML_HEAD +
        YAMAHA_AV_CMD_PUT +
            "<Main_Zone>" +
                "<Surround>" +
                    "<Sound_Program_Param>" +
                        "<MOVIE>" +
                            "<Standard>" +

                                "<DSP_Lvl>" +
                                        "<Val>" + String.valueOf(dspLvl) + "</Val>" +
                                        "<Exp>0</Exp><Unit>dB</Unit>" +
                                "</DSP_Lvl>" +

                                "<Sur_Init_Dly>" +
                                        "<Val>" + String.valueOf(surInitDly) + "</Val>" +
                                        "<Exp>0</Exp><Unit>ms</Unit>" +
                                "</Sur_Init_Dly>" +

                                "<Sur_Room_Size>" +
                                        "<Val>" + String.valueOf(surRoomSize) + "</Val>" +
                                        "<Exp>1</Exp><Unit/>" +
                                "</Sur_Room_Size>" +

                                "<Sur_Liveness>" +
                                        "<Val>" + String.valueOf(surLiveness) + "</Val>" +
                                        "<Exp>0</Exp><Unit/>" +
                                "</Sur_Liveness>" +

                                "<Sur_Back_Init_Dly>" +
                                        "<Val>" + String.valueOf(surBackInitDly) + "</Val>" +
                                        "<Exp>0</Exp><Unit>ms</Unit>" +
                                "</Sur_Back_Init_Dly>" +

                                "<Sur_Back_Room_Size>" +
                                        "<Val>" + String.valueOf(surBackRoomSize) + "</Val>" +
                                        "<Exp>1</Exp><Unit/>" +
                                "</Sur_Back_Room_Size>" +

                                "<Sur_Back_Liveness>" +
                                        "<Val>" + String.valueOf(surBackLiveness) + "</Val>" +
                                        "<Exp>0</Exp><Unit/>" +
                                "</Sur_Back_Liveness>" +

                            "</Standard>" +
                        "</MOVIE>" +
                    "</Sound_Program_Param>" +
                "</Surround>" +
            "</Main_Zone>" +
         YAMAHA_AV_CLOSING;
    }



    /**
     * @param what "NET_RADIO",
     */
    public static String LIST_INFO(String what) {
        return XML_HEAD +
               YAMAHA_AV_CMD_GET +
                   "<" + what + ">" +
                       "<List_Info>GetParam</List_Info>" +
                   "</" + what + ">" +
               YAMAHA_AV_CLOSING;
    }



    /**
     * @param what "NET_RADIO",
     */
    public static String LIST_CLICK(String what, int selection) {
        return XML_HEAD +
               YAMAHA_AV_CMD_PUT +
                   "<" + what + ">" +
                       "<List_Control>" +
                           "<Direct_Sel>" +
                                "Line_" + String.valueOf(selection) +
                           "</Direct_Sel>" +
                       "</List_Control>" +
                   "</" + what + ">" +
               YAMAHA_AV_CLOSING;
    }



    /**
     * @param line  line to jump to (loaded asyncsly)
     * */
    public static String JUMP_LINE(int line) {
        if(line == 0) line = 1;

        return XML_HEAD +
               YAMAHA_AV_CMD_PUT +
                   "<NET_RADIO>" +
                       "<List_Control>" +
                           "<Jump_Line>" +
                                String.valueOf(line) +
                           "</Jump_Line>" +
                       "</List_Control>" +
                   "</NET_RADIO>" +
               YAMAHA_AV_CLOSING;
    }



    /**
     * @param what      "NET_RADIO",
     * @param direction "Up", "Down"
     */
    public static String LIST_DIRECTION(String what, String direction) {
        return XML_HEAD +
               YAMAHA_AV_CMD_PUT +
                   "<" + what + ">" +
                       "<List_Control>" +
                           "<Page>" + direction + "</Page>" +
                       "</List_Control>" +
                   "</" + what + ">" +
               YAMAHA_AV_CLOSING;
    }



    /**
     * @param which "NET_RADIO",
     */
    public static String LIST_RETURN(String which) {
        return XML_HEAD +
               YAMAHA_AV_CMD_PUT +
                   "<" + which + ">" +
                       "<List_Control>" +
                           "<Cursor>Return</Cursor>" +
                       "</List_Control>" +
                   "</" + which + ">" +
               YAMAHA_AV_CLOSING;
    }



    /**
     * @param what "NET_RADIO", "Spotify"
     */
    public static String PLAY_INFO(String what) {
        return XML_HEAD +
               YAMAHA_AV_CMD_GET +
                   "<" + what + ">" +
                        "<Play_Info>GetParam</Play_Info>" +
                   "</" + what + ">" +
                 YAMAHA_AV_CLOSING;
    }



}
