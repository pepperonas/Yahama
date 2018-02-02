/*
 * Copyright (c) 2018 Martin Pfeffer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pepperonas.yahama.app.model;


import android.util.Log;

import com.pepperonas.yahama.app.MainActivity;
import com.pepperonas.yahama.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by martin on 20.07.2015.
 * Amplifier
 */
public class AmpYaRxV577 {

    private static final String TAG = "Amplifier";


    public static final String AV_SPECS = "/YamahaRemoteControl/ctrl";

    public static final String PORT = ":80";

    public static final String RESPONSE_SUCCESS = "RC=\"0\"";

    public static final String ON = "On", OFF = "Off", STANDBY = "Standby";

    public static final float MAX_VOL_SLIDER = 800f;

    private ConfigClassical configClassical;
    private ConfigEntertainment configEntertainment;
    private ConfigLiveClub configLiveClub;
    private ConfigMovie configMovie;
    private List<String> stationList;
    private RadioStation radioStation;


    public AmpYaRxV577() {
        stationList = new ArrayList<String>();
        configClassical = new ConfigClassical();
        configEntertainment = new ConfigEntertainment();
        configLiveClub = new ConfigLiveClub();
        configMovie = new ConfigMovie();
        radioStation = new RadioStation();
    }


    public ConfigEntertainment getConfigEntertainment() {
        return configEntertainment;
    }


    public void setConfigEntertainment(ConfigEntertainment configEntertainment) {
        this.configEntertainment = configEntertainment;
    }


    public ConfigMovie getConfigMovie() {
        return configMovie;
    }


    public void setConfigMovie(ConfigMovie configMovie) {
        this.configMovie = configMovie;
    }


    public ConfigLiveClub getConfigLiveClub() {
        return configLiveClub;
    }


    public void setConfigLiveClub(ConfigLiveClub configLiveClub) {
        this.configLiveClub = configLiveClub;
    }


    public ConfigClassical getConfigClassical() {
        return configClassical;
    }


    public void setConfigClassical(ConfigClassical configClassical) {
        this.configClassical = configClassical;
    }


    public RadioStation getRadioStation() {
        return radioStation;
    }


    public void setRadioStation(RadioStation radioStation) {
        this.radioStation = radioStation;
    }


    /**
     * Play_Control
     */
    public static final String PC_PLAY = "Play", PC_STOP = "Stop", PC_PAUSE = "Pause";

    /**
     * Menu_Control
     */
    public static final String MC_DOWN = "Down", MC_UP = "Up";
    public static final String MC_LEFT = "Left", MC_RIGHT = "Right";
    public static final String MC_RETURN = "Return";
    public static final String MC_DISPLAY = "Display";
    public static final String MC_OPTION = "OPTION";


    /**
     * DSP STRAIGHT
     */
    public static final String DSP_X_STRAIGHT = "Straight";

    /**
     * DSP
     */
    public static final String DSP_2CH_STEREO = "2ch Stereo";
    public static final String DSP_7CH_STEREO = "7ch Stereo";
    public static final String DSP_SURROUND_DECODER = "Surround Decoder";

    public static final String ACTION_GAME = "Action Game";
    public static final String ADVENTURE = "Adventure";
    public static final String CELLAR_CLUB = "Cellar Club";
    public static final String CHAMBER = "Chamber";
    public static final String DRAMA = "Drama";
    public static final String HALL_IN_MUNICH = "Hall in Munich";
    public static final String HALL_IN_VIENNA = "Hall in Vienna";
    public static final String MONO_MOVIE = "Mono Movie";
    public static final String MUSIC_VIDEO = "Music Video";
    public static final String ROLEPLAYING_GAME = "Roleplaying Game";
    public static final String SCIFI = "Sci-Fi";
    public static final String SPECTACLE = "Spectacle";
    public static final String SPORTS = "Sports";
    public static final String STANDARD = "Standard";
    public static final String THE_BOTTOM_LINE = "The Bottom Line";
    public static final String THE_ROXY_THEATRE = "The Roxy Theatre";
    public static final String XML_CELLAR_CLUB = "Cellar_Club";

    public static final String XML_ACTION_GAME = "Action_Game";
    public static final String XML_HALL_IN_MUNICH = "Hall_in_Munich";
    public static final String XML_HALL_IN_VIENNA = "Hall_in_Vienna";
    public static final String XML_SCIFI = "Sci_Fi";
    public static final String XML_MONO_MOVIE = "Mono_Movie";
    public static final String XML_MUSIC_VIDEO = "Music_Video";
    public static final String XML_ROLEPLAYING_GAME = "Roleplaying_Game";
    public static final String XML_THE_BOTTOM_LINE = "The_Bottom_Line";
    public static final String XML_THE_ROXY_THEATRE = "The_Roxy_Theatre";

    /**
     * CATEGORIES
     */
    public static final String CAT_LIVE_CLUB = "LIVE_CLUB";
    public static final String CAT_CLASSICAL = "CLASSICAL";
    public static final String CAT_MOVIE = "MOVIE";
    public static final String CAT_ENTERTAINMENT = "ENTERTAINMENT";


    /**
     * INPUT
     */
    public static String INPUT_AV(int i) {
        return "AV" + String.valueOf(i);
    }


    public static String INPUT_HDMI(int i) {
        return "HDMI" + String.valueOf(i);
    }


    public static final String INPUT_NET_RADIO = "NET RADIO";

    public static final String XML_INPUT_NET_RADIO = "NET_RADIO";

    public static final String XML_INPUT_SPOTIFY = "Spotify";
    public static String INPUT_AUX = "AUX";

    public static String INPUT_TUNER = "TUNER";
    public static String INPUT_DVD = "DVD";
    public static String INPUT_USB = "USB";
    public static String INPUT_AUDIO = "AUDIO";
    public static String INPUT_SERVER = "SERVER";


    /**
     * LIST
     */
    public static final String ARTIST = "Artist";
    public static final String TRACK = "Track";
    public static final String SONG = "Song";
    public static final String STATION = "Station";
    public static final String ALBUM = "Album";


    public String dbgMsg() {
        String dbgMsg = deviceName + " (" + ip + ")\n" +
                "On: " + isOn + "\n" +
                "Sleep: " + sleep + "\n" +
                "Mute: " + isMute + "\n" +
                "Standby: " + isInStandby + "\n" +
                "Volume: " + volume + "\n" +
                "Bass: " + bass + "\n" +
                "Treble: " + treble + "\n" +
                "Enhancer: " + isEnhancer + "\n" +
                "Subwoofer Trim: " + subwooferTrim + "\n" +
                "Input: " + activeInput + "\n" +
                "SoundProg: " + soundProgram + "\n" +
                "Direct: " + isDirect + "\n" +
                "Straight: " + isStraight + "\n" +
                "Adaptive DRC: " + isAdaptiveDrc + "\n" +
                "Extra Bass: " + isExtraBass + "\n" +
                "3D Cinema: " + is_3dCinemaDrc + "\n" +
                "SpeakerA: " + speakerA + "\n" +
                "SpeakerB: " + speakerB + "\n" +
                "Zone: " + activeZone + "\n" +
                "Last Update: " + lastUpdate + "\n" + "\n" +
                getConfigClassical().dbgMsg() + "\n" +
                getConfigEntertainment().dbgMsg() + "\n" +
                getConfigLiveClub().dbgMsg() + "\n" +
                getConfigMovie().dbgMsg() + "\n" +
                "----------------" + "\n" +
                "Station: " + getRadioStation().getStation() + "\n" +
                "Song: " + getRadioStation().getSong();

        Log.i(TAG, dbgMsg);
        return dbgMsg;
    }


    private String ip = "";

    private boolean isOn = false;
    private boolean isMute = false;
    private boolean isInStandby = false;
    private boolean isEnhancer = false;

    private boolean isDirect = false;
    private boolean isStraight = false;
    private boolean isAdaptiveDrc = false;
    private boolean isExtraBass = false;
    private boolean is_3dCinemaDrc = false;

    private boolean speakerA = false;
    private boolean speakerB = false;

    private float volume = -400;
    private int bass = 0;
    private int treble = 0;
    private int subwooferTrim = 0;
    private String activeInput = INPUT_AV(1);
    private String sleep = "";
    private String soundProgram = "";
    private int activeZone = -1;

    private long lastUpdate = -1;

    private String deviceName = "";


    public String getIp() {
        return ip;
    }


    public void setIp(String ip) {
        Log.i(TAG, "SETTING IP: " + ip);
        this.ip = ip;
    }


    public String getActiveInput() {
        return activeInput;
    }


    public void setActiveInput(String activeInput) {
        this.activeInput = activeInput;
    }


    public boolean isOn() {
        return isOn;
    }


    public void setOn(boolean isOn) {
        this.isOn = isOn;
    }


    public boolean isMute() {
        return isMute;
    }


    public void setMute(boolean isMute) {
        this.isMute = isMute;
    }


    public boolean inStandby() {
        return isInStandby;
    }


    public void setStandby(boolean isInStandby) {
        this.isInStandby = isInStandby;
    }


    public float getVolume() {
        return volume / 10;
    }


    public void setVolume(float volume) {
        this.volume = volume;
        if (this.volume >= 0) this.volume *= -1;
    }


    public int getActiveZone() {
        return activeZone;
    }


    public void setActiveZone(int activeZone) {
        this.activeZone = activeZone;
    }


    public long getLastUpdate() {
        return lastUpdate;
    }


    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }


    public void initAmpVars(MainActivity main, String s) {
        setLastUpdate(System.currentTimeMillis());
        setOn(s.contains("<Power>On"));
        setMute(s.contains("<Mute>On"));
        setStandby(s.contains("<Standby_Through_Info>On"));
        setActiveInput(s.split("<Input_Sel>")[1].split("<")[0]);
        setVolume(parseVolume(s));
        if (s.contains("<Volume><Lvl><Val>")) {
            float volume = getVolumeFromResponse(s.split("<Volume><Lvl><Val>")[1]);
            main.updateVolumeSlider(volume);
        }
        if (s.contains("<Sound_Video><Tone><Bass><Val>")) {
            setBass(parseBass(s));
        }
        setTreble(parseTreble(s));
        setSubwooferTrim(parseSubwooferTrim(s));
        setIsExtraBass(s.contains("<Extra_Bass>Auto"));
        setIsDrcAuto(s.contains("<Adaptive_DRC>Auto"));
        setIsDirect(s.contains("<Direct><Mode>On"));
        setEnhancer(s.contains("<Enhancer>On"));
        setIs_3dCinemaDsp(s.contains("<_3D_Cinema_DSP>Auto"));
        setIsStraight(s.contains("<Current><Straight>On"));
        if (s.contains("<Sleep>")) {
            String sleep = s.split("<Sleep>")[1].split("</Sleep>")[0];
            setSleep(sleep);
        }
        if (s.contains("<Sound_Program>")) {
            setSoundProgram(s.split("<Sound_Program>")[1].split("</Sound_Program")[0]);
        }
        setSpeakerA(s.contains("<Speaker_A>On"));
        setSpeakerB(s.contains("<Speaker_B>On"));

    }


    private void setSoundProgram(String soundProgram) {
        this.soundProgram = soundProgram;
    }


    private void setSleep(String sleep) {
        this.sleep = sleep;
    }


    private String getSleep() {
        return sleep;
    }


    private void setIsStraight(boolean isStraight) {
        this.isStraight = isStraight;
    }


    private void setIs_3dCinemaDsp(boolean is_3dCinemaDsp) {
        this.is_3dCinemaDrc = is_3dCinemaDsp;
    }


    public void setEnhancer(boolean isEnhancer) {
        this.isEnhancer = isEnhancer;
    }


    private void setSubwooferTrim(int subwooferTrim) {
        this.subwooferTrim = subwooferTrim;
    }


    private void setIsDirect(boolean isDirect) {
        this.isDirect = isDirect;
    }


    private void setIsDrcAuto(boolean isDrcAuto) {
        this.isAdaptiveDrc = isDrcAuto;
    }


    private void setIsExtraBass(boolean isExtraBass) {
        this.isExtraBass = isExtraBass;
    }


    public void setTreble(int treble) {
        this.treble = treble;
    }


    public void setBass(int bass) {
        this.bass = bass;
    }


    public String getInputAsXml() {
        if (activeInput.equals("NET RADIO")) return INPUT_NET_RADIO;
        else return activeInput;
    }


    public String getDeviceName() {
        return deviceName;
    }


    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }


    public boolean isDirect() {
        return isDirect;
    }


    public boolean isEnhancer() {
        return isEnhancer;
    }


    public boolean isExtraBass() {
        return isExtraBass;
    }


    public boolean isAdaptiveDrc() {
        return isAdaptiveDrc;
    }


    public boolean is_3dCinemaDrc() {
        return is_3dCinemaDrc;
    }


    public boolean isStraight() {
        return isStraight;
    }


    public int getBass() {
        return bass;
    }


    public int getTreble() {
        return treble;
    }


    public float getVolumeFromResponse(String s) {
        String volStr = s;
        volStr = volStr.split("</")[0];
        return Integer.parseInt(volStr) / 10f;
    }


    public int getTrebleFromTrailer(String s) {
        return Integer.parseInt(s);
    }


    public int getBassFromTrailer(String s) {
        return Integer.parseInt(s);
    }


    public float getVolumeFromTrailer(String s) {
        return Integer.parseInt(s) / 10f;
    }


    public static int parseVolume(String response) {
        if (!response.contains("<Lvl><Val>")) {
            return -1000;
        }
        String s[] = response.split("<Lvl><Val>");
        return Integer.parseInt(s[1].split("</Val>")[0].replace("-", ""));
    }


    public static int parseBass(String response) {
        if (!response.contains("<Sound_Video><Tone><Bass><Val>")) {
            return 0;
        }
        String s[] = response.split("<Sound_Video><Tone><Bass><Val>");
        return Integer.parseInt(s[1].split("</Val>")[0]);
    }


    public static int parseTreble(String response) {
        if (!response.contains("<Treble><Val>")) {
            return 0;
        }
        String s[] = response.split("<Treble><Val>");
        return Integer.parseInt(s[1].split("</Val>")[0]);
    }


    public static int parseSubwooferTrim(String response) {
        if (!response.contains("<Subwoofer_Trim><Val>")) {
            return 0;
        }
        String s[] = response.split("<Subwoofer_Trim><Val>");
        return Integer.parseInt(s[1].split("</Val>")[0]);
    }


    public static float getVolume_dB(int progress) {
        float realProgress = (AmpYaRxV577.MAX_VOL_SLIDER - progress) / 10f;
        return Utils.roundToHalf(realProgress) * -1;
    }


    public static String getCategory(String which) {

        Log.w(TAG, "getCategory " + which);

        if (Utils.stringEquals(
                which,
                AmpYaRxV577.XML_CELLAR_CLUB,
                AmpYaRxV577.XML_THE_BOTTOM_LINE,
                AmpYaRxV577.XML_THE_ROXY_THEATRE)) {

            Log.d(TAG, "getCategory ~ returns: " + AmpYaRxV577.CAT_LIVE_CLUB);
            return AmpYaRxV577.CAT_LIVE_CLUB;
        }

        if (Utils.stringEquals(
                which,
                AmpYaRxV577.XML_ACTION_GAME,
                AmpYaRxV577.XML_ROLEPLAYING_GAME,
                AmpYaRxV577.XML_MUSIC_VIDEO,
                AmpYaRxV577.SPORTS)) {

            Log.d(TAG, "getCategory ~ returns: " + AmpYaRxV577.CAT_ENTERTAINMENT);
            return AmpYaRxV577.CAT_ENTERTAINMENT;
        }

        if (Utils.stringEquals(
                which,
                AmpYaRxV577.XML_HALL_IN_MUNICH,
                AmpYaRxV577.XML_HALL_IN_VIENNA,
                AmpYaRxV577.CHAMBER)) {

            Log.d(TAG, "getCategory ~ returns: " + AmpYaRxV577.CAT_CLASSICAL);
            return AmpYaRxV577.CAT_CLASSICAL;
        } else {
            Log.d(TAG, "getCategory ~ returns: " + AmpYaRxV577.CAT_MOVIE);
            return AmpYaRxV577.CAT_MOVIE;
        }
    }


    public void setSpeakerA(boolean speakerA) {
        this.speakerA = speakerA;
    }


    public void setSpeakerB(boolean speakerB) {
        this.speakerB = speakerB;
    }


    public boolean isSpeakerA() {
        return speakerA;
    }


    public boolean isSpeakerB() {
        return speakerB;
    }


    public List<String> collectListInfo(String radioInfo) {
        stationList.clear();
        String[] info = radioInfo.split("<Txt>");
        for (int i = 1; i < info.length; i++) {
            String entry = info[i].split("</")[0];
            /*
             * check if empty!!!
             */
            if (!entry.isEmpty()) {
                stationList.add(entry);
            }
        }
        return stationList;
    }


    public List<String> getStationList() {
        return stationList;
    }

}
