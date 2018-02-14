package me.milechen.qicheng.Utils.Net;

/**
 * Created by mile on 2017/7/8.
 */
public class Urls {
    public static final String ROOT = "http://qicheng.milez.ml/";
    //public static final String ROOT = "http://192.168.31.207/~mile/qicheng/";

    public static final String GET_HOT_TALE_VIEW = ROOT+"index.php/Api/Tale/getHotTaleView";
    public static final String GET_LATEST_TALE_VIEW = ROOT+"index.php/Api/Tale/getLatestTaleView";
    public static final String NEW_TALE = ROOT+"index.php/Api/Tale/newTale";

    public static final String GET_TAGS = ROOT+"index.php/Api/Tag/getTags";

    public static final String LOGIN_BY_ID = ROOT+"index.php/Api/User/loginById";
    public static final String GET_AVATAR_BY_PHONE = ROOT+"index.php/Api/User/getAvatarByPhone";
    public static final String GET_NICK_BY_PHONE = ROOT+"index.php/Api/User/getNickByPhone";
    public static final String LOGIN = ROOT+"index.php/Api/User/login";
    public static final String UPDATE_CLIENT_ID = ROOT+"index.php/Api/User/updateClientId";
    public static final String REGISTER = ROOT + "index.php/Api/User/newUser";

    public static final String GET_TALE = ROOT+"index.php/Api/Tale/getCertainTaleView";
    public static final String IS_USER_FOCUS_TALE = ROOT+"index.php/Api/Tale/isUserFocusTale";
    public static final String TOGGLE_FOCUS = ROOT+"index.php/Api/Tale/toggleFocus";
    public static final String GET_PARAS_OF_A_TALE = ROOT+"index.php/Api/Tale/getParasOfTale";
    public static final String GET_PARA_ZAN_NUM = ROOT+"index.php/Api/Tale/getParaZanNum";
    public static final String TOGGLE_ZAN = ROOT+"index.php/Api/Tale/toggleZan";
    public static final String IS_USER_ZAN_PARA = ROOT+"index.php/Api/Tale/isParaZanedByUser";
    public static final String WRITE_PARA = ROOT+"index.php/Api/Tale/writePara";
    public static final String VOTE_TO_DELETE = ROOT+"index.php/Api/Tale/voteToDelete";
    public static final String SET_USER_ACTIVE_IN_TALE = ROOT+"index.php/Api/Tale/setActive";
    public static final String UNSET_USER_ACTIVE_IN_TALE = ROOT+"index.php/Api/Tale/unsetActive";
    public static final String SET_TALE_SILENT = ROOT+"index.php/Api/Tale/setSilent";
    public static final String UNSET_TALE_SILENT = ROOT+"index.php/Api/Tale/unsetSilent";
    public static final String ASK_FOR_PUSH_TALE_STATUS = ROOT+"index.php/Api/Tale/askPushStatus";
    public static final String GET_TALE_SPEECHES_OLD = ROOT+"index.php/Api/Tale/getSpeechesOld";
    public static final String GET_TALE_SPEECHES_NEW = ROOT+"index.php/Api/Tale/getSpeechesNew";
    public static final String ADD_TALE_SPEECHES = ROOT+"index.php/Api/Tale/addSpeech";

    public static final String GET_GROUP_BY_USER = ROOT+"index.php/Api/Group/getGroupByUser";
    public static final String GET_GROUP_VIEW = ROOT+"index.php/Api/Group/getGroupView";
    public static final String GET_PARAS_OF_A_GROUP = ROOT+"index.php/Api/Group/getParaOfGroup";
    public static final String SET_USER_ACTIVE_IN_GROUP = ROOT+"index.php/Api/Group/setActive";
    public static final String UNSET_USER_ACTIVE_IN_GROUP = ROOT+"index.php/Api/Group/unsetActive";
    public static final String SET_GROUP_SILENT = ROOT+"index.php/Api/Group/setSilent";
    public static final String UNSET_GROUP_SILENT = ROOT+"index.php/Api/Group/unsetSilent";
    public static final String ASK_FOR_PUSH_GROUP_STATUS = ROOT+"index.php/Api/Group/askPushStatus";
    public static final String GET_GROUP_SPEECH_OLD = ROOT+"index.php/Api/Group/getSpeechesOld";
    public static final String GET_GROUP_SPEECH_NEW = ROOT+"index.php/Api/Group/getSpeechesNew";
    public static final String ADD_GROUP_SPEECHES = ROOT+"index.php/Api/Group/addSpeech";
    public static final String WRITE_GROUP_PARA = ROOT+"index.php/Api/Group/writeParaOfAGroup";
    public static final String JOIN_GROUP = ROOT+"index.php/Api/Group/joinGroup";
    public static final String QUIT_GROUP = ROOT+"index.php/Api/Group/quitGroup";
    public static final String CREATE_GROUP = ROOT+"index.php/Api/Group/newGroup";
    public static final String GET_GROUP_MEMBERS = ROOT+"index.php/Api/Group/getGroupMembers";



}
