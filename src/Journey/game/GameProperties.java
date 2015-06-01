package Journey.game;

/**
 * Enumeration outlining the data in the properties xml file and other data
 * sources.
 * 
 * @author 
 *          Anthony G. Musco
 * <dt><b>Date Created</b><dd>
 *          Oct-22-2014
 * <dt><b>Class</b><dd>
 *          CSE 219
 */
public enum GameProperties {
    
    /* Properties and Schema File Names */
    PROPERTIES_FILE_NAME, SCHEMA_FILE_NAME, ABOUT_GAME_FILE,
    
    /* Directories for Files */
    DATA_PATH, IMG_PATH, AUDIO_PATH, LOAD_FILE_NAME,
    
    /* Dimensions */
    WINDOW_WIDTH, WINDOW_HEIGHT, PANE_WIDTH, PANE_HEIGHT, EDIT_PANE_WIDTH,
    BUTTON_WIDTH, BUTTON_HEIGHT,
    
    /* Map File */
    MAP_FILE_NAME, MAP_GLOBAL_IMAGE, CITY_NODE_TAG, NAME_TAG, QUARTER_TAG,
    COLOR_TAG, FLIGHT_TAG, XPOS_TAG, YPOS_TAG, FLIGHTX_TAG, FLIGHTY_TAG, 
    EDGES_TAG, INSTRUCTIONS_TAG, CARD_FRONT_TAG, CARD_BACK_TAG, FLIGHT_MAP_FILE,
    
    /* Game Images */
    SPLASH_SCREEN_IMAGE, PIECE_BLUE, PIECE_RED, PIECE_BLACK, PIECE_WHITE,
    PIECE_YELLOW, PIECE_GREEN, BACK_IMAGE, UNDO_IMAGE, STATS_IMAGE,
    TIME_IMAGE, GAME_EDIT_PANE_BG, RED_CITY_SELECTED, RED_CITY_UNSELECTED,
    GREEN_CITY_SELECTED, GREEN_CITY_UNSELECTED, YELLOW_CITY_SELECTED, 
    YELLOW_CITY_UNSELECTED, BACKGROUND_IMAGE, INDICATOR_IMAGE, MUSIC_ON_IMAGE,
    MUSIC_OFF_IMAGE,
    
    /* Game Text */
    GAME_TITLE_TEXT, GAME_SUBHEADER_TEXT, NEW_GAME_TEXT, LOAD_GAME_TEXT,
    HELP_TEXT, ABOUT_GAME_TEXT, EXIT_TEXT, BACK_TEXT,   
    PLAY_TEXT, LETTER_FONT_FAMILY_TEXT, LETTER_FONT_SIZE_TEXT,
    DEFAULT_YES_TEXT, DEFAULT_NO_TEXT, DEFAULT_EXIT_TEXT,
    
    /* Game Sounds */
    THEME_MUSIC_FILE,
    
    /* Game Constants */
    MAX_PLAYERS,
    
    /* Sound Effects */
    GAME_MUSIC_MP3, GAME_OVER_MP3

}
