package com.mc.utilities;


public interface Constant {

    boolean SERVER_LIVE = false;

    String AUTHORIZATION = "Authorization";
    String BEARER = "Bearer ";
    String ACCESS_TOKEN = "accessToken";

    String KEY_INSTALLED_APP = "KEY_INSTALLED_APP";
    String KEY_TOKEN = "KEY_TOKEN";
    String KEY_USER_KEYCLOAK = "KEY_USER_KEYCLOAK";
    String KEY_USER_ID = "KEY_USER_ID";
    String KEY_REFRESH_TOKEN = "refresh_token";
    String KEY_ACCESS_TOKEN = "accessToken";
    String KEY_REDIRECT_URL = "REDIRECT_URL";
    String KEY_USER_INFO = "KEY_USER_INFO";
    String KEY_USER = "KEY_USER";
    String KEY_AUDIO = "audio";
    String KEY_VIDEO = "video";
    String KEY_PDF = "pdf";
    String KEY_EXAM = "exam";
    String KEY_COURSE = "course";
    String KEY_RELETE_BOOK = "KEY_RELETE_BOOK";
    String KEY_LIST_QUESTION = "KEY_LIST_QUESTION";
    String KEY_QUESTION = "KEY_QUESTION";
    String KEY_POSITION = "KEY_POSITION";
    String KEY_LIST_LESSON = "KEY_LIST_LESSON";
    String KEY_LIST_GIFT = "KEY_LIST_GIFT";
    String KEY_INDEX = "KEY_INDEX";
    String KEY_TIME = "KEY_TIME";
    String KEY_EXAM_ID = "KEY_EXAM_ID";
    String KEY_ANDROID_SHARE = "KEY_ANDROID_SHARE";
    String KEY_OPEN_INSTRUCTION = "KEY_OPEN_INSTRUCTION";
    String KEY_MORE = "KEY_MORE";
    String KEY_POINT = "KEY_POINT";
    String KEY_NOTI_INFO = "KEY_NOTI_INFO";
    String KEY_COUNT_NOTI = "KEY_COUNT_NOTI";

    String KEY_COMMON_BOOK_NOTI = "common";
    String KEY_COURSES_BOOK_NOTI = "courses";
    String KEY_EXERCISES_BOOK_NOTI = "exercises";
    String KEY_TYPE_NOTI_MYBOOK = "mybook";
    String KEY_TYPE_NOTI_MANUAL = "manual";
    String KEY_TYPE_NOTI_DAYS = "notiDaily";
    String KEY_NOTIFICATION_EVENT = "event";


    String FORMAT_DATE_DISPLAY = "dd/MM/yyyy";
    String MALE = "1";
    String FEMALE = "0";
    String OTHER = "2";
    String KEY_BOOK_TAB = "KEY_BOOK_TAB";
    String KEY_BOOK_INFOMATION = "KEY_BOOK_INFOMATION";
    String KEY_LIST_RELATED_BOOK = "KEY_LIST_RELATED_BOOK";
    String KEY_CHAPTER = "KEY_CHAPTER";
    String KEY_INDEX_LESSON = "KEY_INDEX_LESSON";
    String KEY_LESSON = "KEY_LESSON";
    String KEY_GIFT = "KEY_GIFT";
    String KEY_QR = "KEY_QR";
    String KEY_OPEN_DIALOG = "KEY_OPEN_DIALOG";

    int AVATAR_SIZE = 250;
    String KEY_TYPE_BODY_IMG_REQUEST = "image/jpeg";
    String IMAGE_JPG = ".jpg";
    String KEY_FILE = "file";
    String KEY_TYPE_UPLOAD = "users-avatar";
    String BOOK_ID = "book_id";
    String GIFT_ID = "gift_id";
    String BOOK_NAME = "book_name";
    String TRAINING_CHAPTER_ID = "training_chapter_id";


    String SINGLE_CHOOSE = "correctAnswer";
    String MIX_SINGLE_CHOOSE = "mixCorrectAnswer";
    String FILL_WORD = "wordFill";
    String MIX_FILL_WORD = "mixWordFill";
    String MATCHING = "matching";
    String MIX_MATCHING = "mixMatching";

    String QUESTION_CORRECT = "Correct";
    String QUESTION_INCORRECT = "InCorrect";
    String QUESTION_NOT_ANSWER = "NotAnswer";
    String QUESTION_ANSWER = "Answer";

    String AUDIO = "audio";
    String VIDEO = "video";
    String IMAGE = "image";
    String TXT = ".txt";

    int TYPE_QR_BOOK = 0;
    int TYPE_QR_LESSON = 1;


    int LINE_SHOW_MORE = 5;
    int MAX_LINE = 5;
    int MAXIUM_LINE = 99;
    int MAX_LENGHT = 20;


    int HOME_TAB = 110;
    int GIFT_TAB = 111;
    int NOTIFICATION_TAB = 112;
    int MORE_TAB = 113;

    String EASY_EXAMS = "easy";
    String MEDIUM_EXAMS = "medium";
    String HARD_EXAMS = "hard";
    String HARDEST_EXAMS = "hardest";

    int LIMIT_PAGING = 20;
    int LIMIT_START = 0;

    String ALL_FILTER = "all";
    String FRIEND_FILTER = "myfriend";
    String DATE_FILTER = "date";
    String MONTH_FILTER = "month";
    String WEEK_FILTER = "week";
    String YEAR_FILTER = "year";
    String BOOK_FILTER = "mybook";

    // json offline
    String MY_BOOK = "mybook";
    String INFO_BOOK = "infomationBook";
    String DO_TRAINING = "dotraining";
    String MY_GIFT = "mygift";
    String MY_LESSON = "mylesson";
    String JSON = "JSON";

    //folder donwload
    String FOLDER_GIFT = "gift";
    String FOLDER_BOOK = "book";
    String FOLDER_REALM = "realm";
    int NONE_DOWNLOAD = -1;
    int PENDIND_DOWNLOAD = 0;
    int DOWNLOADING = 1;
    int DOWNLOADED = 2;
    int DOWNLOAD_ERROR = 3;


    //do training
    String KEY_SUBTITLE = "KEY_SUBTITLE";
    String KEY_TRAININGAUDIO = "KEY_TRAININGAUDIO";
    String KEY_BACK_RECORDER = "KEY_BACK_RECORDER";
    String KEY_BACKGROUND_TRAINING = "KEY_BACKGROUND_TRAINING";
    String KEY_RECORD_DETAIL = "KEY_RECORD_DETAIL";
    String KEY_LIST_ROLE = "KEY_LIST_ROLE";
    String KEY_BACK_TRAINING = "KEY_BACK_TRAINING";
    String KEY_CONVERSATION = "KEY_CONVERSATION";
    String KEY_RESET_AUDIO = "KEY_RESET_AUDIO";
    String FILE_AUDIO = ".3gp";
    String KEY_DOT = ":";
    String KEY_THREE_DOT = "...";


    int DOWNLOAD_FILE = 0;
    int DOWNLOAD_CHAPTER = 1;
    int DOWNLOAD_ALL = 2;

    String HTTP = "http";
    String HTTPS = "https";
    String DEVICE = "Device";
    String ANDROID = "android";

    String DISABLE_RECEIVE_NOTI = "RECEIVE_NOTI";
    String DISABLE_SOUND_NOTI = "SOUND_NOTI";

    String TAG_P = "<p>";
    String TAG_P_1 = "</p>";

    String CHILD = "child";
    String QUESTION ="question";
}
