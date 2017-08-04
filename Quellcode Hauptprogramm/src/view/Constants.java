package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;

/**
 * Diese Klasse dient zum Sammeln von Konstanten welche fuer das Zeichen der UI
 * ausschlaggebend sind.
 */
public class Constants {

    // === Liniendicken ===
    public static final Stroke UNSELECTED_CONNECTIONS_LINE_STROKE = new BasicStroke(2f);
    public static final Stroke SELECTED_CONNECTIONS_LINE_STROKE = new BasicStroke(3.5f);
    public static final Stroke NODE_BORDERS_LINE_STROKE = new BasicStroke(1.35f);
    public static final Stroke HIGHLIGHTED_NODE_BORDERS_LINE_STROKE = new BasicStroke(2.0f);
    public static final Stroke TOOLBAR_BORDER_LINE_STROKE = new BasicStroke(1.0f);
    public static final Stroke ONE_PX_LINE_STROKE = new BasicStroke(1.0f);
    public static final Stroke ONE_POINT_FIVE_PX_LINE_STROKE = new BasicStroke(1.5f);
    public static final Stroke TWO_PX_LINE_STROKE = new BasicStroke(2.0f);
    public static final Stroke TREE_PX_LINE_STROKE = new BasicStroke(3.0f);
    public static final Stroke FOUR_PX_LINE_STROKE = new BasicStroke(4.0f);
    public static final Stroke FIVE_PX_LINE_STROKE = new BasicStroke(5.0f);
    public static final Stroke SIX_PX_LINE_STROKE = new BasicStroke(6.0f);

    // === Abmasse ===
    public static final int CORNER_EXECUTABLE_NODES = 5;
    public static final int NODE_EDGE_ROUNDING = 30;
    public static final int NODE_INFO_EDGE_ROUNDING = 25;
    public static final int NODE_INFO_GAP = 10;
    public static final int INFO_SECTION_HEIGHT = 200;
    public static final int TOOLBAR_HEIGHT = 65;
    public static final int TOOLBAR_ITEM_WIDTH = 88;
    public static final int TOOLBAR_ITEM_PRESSED_DOWN_OFFSET_XY = 3;
    public static final int MINIMUM_NODE_HEIGHT = 75;
    public static final int SPACE_BETWEEN_NODE_PREVIEWS = 5;
    public static final int SCROLLBAR_WIDTH = 15;
    public static final int CONNECT_OVERLAY_ROUNDING = 30;
    public static final int CONNECT_OVERLAY_CONNECTION_SPACE = 150;
    public static final int CONNECT_OVERLAY_WIDTH = 760;
    public static final int NODE_Y_DISTANCE_BETWEEN_LETS = 15;
    public static final int NODE_X_DISTANCE_BETWEEN_LET_AND_BORDER = 5;
    public static final int MESSAGE_PADDING_X = 7;
    public static final int MESSAGE_PADDING_Y = 15;
    public static final int ADDITIONAL_DESCRIPTION_PADDING_X = 4;
    public static final int ADDITIONAL_DESCRIPTION_PADDING_Y = 5;
    public static final int MESSAGE_LINE_HEIGHT = 20;
    public static final int ADDITIONAL_DESCRIPTION_LINE_HEIGHT = 15;
    public static final int CONNECTION_ARROW_START_END_LENGTH = 20;
    public static final int RIGHT_CLICK_MENUE_ITEM_HEIGHT = 30;
    public static final int DISTANCE_BETWEEN_NODE_AND_HIS_RELATIVE_NODE = 50;
    public static final int DEBUG_CONTEXT_TABLE_CELL_HEIGHT = 30;
    public static final int DEBUG_CONTEXT_TABLE_CELL_PADDING_X = 10;
    public static final int DEBUG_CONTEXT_TABLE_X_RIGHT_SPACE = 10;
    public static final int DEBUG_CONTEXT_TABLE_Y = 75;

    // === Farben ===
    public static final Color DEFAULT_BACKGROUND_COLOR = new Color(239, 239, 239);
    public static final Color DEFAULT_BACKGROUND_GRID_COLOR = new Color(232, 232, 232);
    public static final Color BLUEPRINT_BACKGROUND_COLOR = new Color(130, 179, 255);
    public static final Color BLUEPRINT_BACKGROUND_GRID_COLOR = new Color(120, 169, 255);
    public static final Color BEETWEEN_TOOLBAR_ITEMS_COLOR = new Color(214, 214, 214);
    public static final Color TOOLBAR_ITEM_PRESSED_DOWN_BORDER_COLOR = new Color(205, 205, 205);
    public static final Color TOOLBAR_ITEM_PRESSED_DOWN_BACKGROUND_COLOR = new Color(225, 225, 225);
    public static final Color TOOLBAR_BACKGROUND_COLOR = Color.WHITE;
    public static final Color NODE_BACKGROUND_COLOR_WHEN_FOCUSING = new Color(255, 255, 255, 80);
    public static final Color NODE_BACKGROUND_COLOR = Color.WHITE;
    
    public static final Color STARTUP_COLOR_DARK = new Color(0, 0, 0, 100);
    public static final Color STARTUP_COLOR_BRIGHT = new Color(255, 255, 255, 130);
    
    public static final Color DEBUGGER_NODE_OTHER_INDICATOR_COLOR = Color.GRAY;
    
    public static final Color DEBUGGER_NODE_PREPARING_BACKGROUND_COLOR = new Color(231, 232, 200);
    public static final Color DEBUGGER_NODE_COLLECTING_BACKGROUND_COLOR = new Color(203, 204, 140);
    public static final Color DEBUGGER_NODE_RUNNING_BACKGROUND_COLOR = new Color(201, 200, 232);
    public static final Color DEBUGGER_NODE_DELIEVERING_BACKGROUND_COLOR = new Color(188, 234, 252);
    public static final Color DEBUGGER_NODE_FINISHED_BACKGROUND_COLOR = new Color(188, 244, 188);
    public static final Color DEBUGGER_NODE_FAILED_BACKGROUND_COLOR = new Color(255, 100, 100);
    public static final Color DEBUGGER_NODE_UNKNOWN_BACKGROUND_COLOR = Color.WHITE;
    
    public static final Color DEBUGGER_INLET_BORDER_COLOR = new Color(91, 137, 87, 180);
    public static final Color DEBUGGER_INLET_BACKGROUND_COLOR = new Color(188, 234, 188, 180);

    public static final Color DEBUGGER_CONTEXT_TABLE_BACKGROUND_COLOR = /*new Color(230, 230, 230, 100);*/new Color(230, 230, 230);
    public static final Color DEBUGGER_CONTEXT_TABLE_ACTUAL_BACKGROUND_COLOR = /*new Color(200, 200, 230);*/Color.WHITE;
    public static final Color DEBUGGER_CONTEXT_TABLE_BORDER_COLOR = Color.BLACK;
    public static final Color DEBUGGER_CONTEXT_TABLE_LABEL_COLOR = Color.BLACK;

    public static final Color NODE_INFO_BACKGROUND_COLOR = Color.WHITE;
    public static final Color HIGHLIGHTED_NODE_BACKGROUND_COLOR = Color.WHITE; // Gelb: new Color(255, 250, 150), Blau: new Color(150, 200, 255)
    public static final Color NODE_BORDER_COLOR = Color.BLACK;
    public static final Color DEFAULT_LINE_COLOR = Color.BLACK;
    public static final Color DEFAULT_FONT_COLOR = Color.BLACK;
    public static final Color NODE_LABEL_COLOR = Color.BLACK;

    public static final Color SCROLLBAR_BACKGROUND_COLOR = new Color(255, 255, 255, 150);
    public static final Color SCROLLBAR_COLOR = Color.WHITE;
    public static final Color SCROLLBAR_BORDER_COLOR = Color.BLACK;

    public static final Color TARGET_COLOR_PRESSED = new Color(255, 255, 255, 180);
    public static final Color TARGET_BORDER_COLOR_PRESSED = new Color(0, 0, 0, 230);
    public static final Color TARGET_COLOR = new Color(255, 255, 255, 80);
    public static final Color TARGET_BORDER_COLOR = new Color(0, 0, 0, 100);

    public static final Color CONNECT_OVERLAY_BACKGROUND_COLOR = new Color(0, 0, 0, 200);
    public static final Color CONNECT_OVERLAY_TEXT_COLOR = Color.WHITE;

    public static final Color DEBUG_NODE_OVERLAY_BACKGROUND_COLOR = CONNECT_OVERLAY_BACKGROUND_COLOR;
    public static final Color DEBUG_NODE_OVERLAY_TEXT_COLOR = CONNECT_OVERLAY_TEXT_COLOR;
    public static final Color DEBUG_NODE_OVERLAY_INACTIVE_TEXT_COLOR = new Color(255, 255, 255, 100);

    public static final Color MESSAGE_BACKGROUND_COLOR = Color.WHITE;
    public static final Color MESSAGE_ERROR_BACKGROUND_COLOR = new Color(249, 137, 137);
    public static final Color DRAG_CONNECTION_LINE_COLOR_FRONT = new Color(0, 0, 0, 190);
    public static final Color ADDITIONAL_DESCRIPTION_KEY_COLOR = new Color(90, 90, 90);
    public static final Color DRAG_CONNECTION_LINE_COLOR_BACK = DEFAULT_BACKGROUND_COLOR;
    public static final Color LOG_BACKGROUND_COLOR = Color.BLACK;
    public static final Color LOG_OUT_TEXT_COLOR = Color.WHITE;
    public static final Color LOG_ERR_TEXT_COLOR = new Color(225, 89, 89);
    public static final Color RIGHT_CLICK_MENUE_FRONT_COLOR = Color.BLACK;
    public static final Color RIGHT_CLICK_MENUE_BACKGROUND_COLOR = Color.WHITE;
    public static final Color RIGHT_CLICK_MENUE_PRESSED_COLOR = new Color(205, 205, 205);

    public static final Color PROJECT_RUNNING_OVERLAY_COLOR = new Color(239, 239, 239, 110);
    public static final Color DEBUG_PROJECT_RUNNING_OVERLAY_COLOR = new Color(236, 229, 183, 30);
    public static final Color PROJECT_FAILED_OVERLAY_COLOR = new Color(255, 90, 90, 80);
    public static final Color PROJECT_SUCCEEDED_OVERLAY_COLOR = new Color(0, 255, 0, 20);

    public static final Color[] RUNTIME_CHART_DIAGRAM_COLORS = new Color[]{
        new Color(212, 185, 187),
        new Color(187, 212, 185),
        new Color(212, 210, 185),
        new Color(210, 185, 212),
        new Color(212, 196, 185),
        new Color(218, 232, 203)
    };

    public static final Color CATEGORY_ACTIVE_COLOR = Color.BLACK;
    public static final Color CATEGORY_NOT_ACTIVE_COLOR = Color.LIGHT_GRAY;

    // === Schriften ===
    public static final Font NODE_LABEL_FONT = new Font("Arial", Font.PLAIN, 12);
    public static final Font TOOLBAR_ITEM_LABEL_FONT = new Font("Arial", Font.PLAIN, 12);
    public static final Font INFO_LABEL_FONT = new Font("Arial", Font.PLAIN, 12);
    public static final Font INFO_IN_OUT_TITLE_FONT = new Font("Arial", Font.BOLD + Font.ITALIC, 12);
    public static final Font INFO_IN_OUT_CAPABILITIES_FONT = new Font("Arial", Font.ITALIC, 12);
    public static final Font SIMPLE_NODE_LABEL_FONT = new Font("Arial", Font.PLAIN, 15);
    public static final Font CONNECTION_LABEL_FONT = new Font("Arial", Font.PLAIN, 15);

    public static final Font CONNECT_OVERLAY_TITLE_FONT = new Font("Arial", Font.BOLD, 14);
    public static final Font CONNECT_OVERLAY_FONT = new Font("Arial", Font.ITALIC, 14);

    public static final Font DEBUG_NODE_OVERLAY_TITLE_FONT = CONNECT_OVERLAY_TITLE_FONT;
    public static final Font DEBUG_NODE_OVERLAY_FONT = CONNECT_OVERLAY_FONT;

    public static final Font MESSAGE_FONT = new Font("Arial", Font.ITALIC, 14);
    public static final Font TARGET_POSITION_FONT = new Font("Arial", Font.BOLD, 8);
    public static final Font ADDITIONAL_DESCRIPTION_KEY_FONT = new Font("Arial", Font.BOLD, 11);
    public static final Font ADDITIONAL_DESCRIPTION_LABEL_FONT = new Font("Arial", Font.PLAIN, 11);
    public static final Font NODE_CONNECTION_NAME_FONT = new Font("Arial", Font.ITALIC, 11);
    public static final Font LOG_TEXT_FONT = new Font("Courier New", Font.PLAIN, 13);
    public static final Font LOG_TEXT_FONT_BOLD = new Font("Courier New", Font.BOLD, 13);
    public static final Font RIGHT_CLICK_MENUE_FONT = new Font("Arial", Font.PLAIN, 12);
    public static final Font DEVELOPER_INFO_FONT = new Font("Arial", Font.PLAIN, 10);
    public static final Font DIRECT_INPUT_FONT = new Font("Arial", Font.PLAIN, 11);
    public static final Font NODE_TAG_FONT = new Font("Arial", Font.BOLD, 13);

    public static final Font DEBUGGER_CONTEXT_TABLE_CONTEXT_CREATOR_FONT = new Font("Arial", Font.BOLD, 13);
    public static final Font DEBUGGER_CONTEXT_TABLE_CONTEXT_FONT = new Font("Arial", Font.PLAIN, 13);
    
    public static final Font DEBUGGER_BLOCK_REASON_FONT = new Font("Arial", Font.ITALIC+Font.BOLD, 13);

    public static final Font CATEGORY_LABEL_FONT = new Font("Arial", Font.PLAIN, 12);

    public static final Font SHOW_VALUE_ARRAY_FONT = new Font("Arial", Font.PLAIN, 11);

    // === Strings ===
    public static final String DEFAULT_NODE_ICON_ASSET_NAME = "Coding_30px.png";
}
