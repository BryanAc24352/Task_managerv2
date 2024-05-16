package com.os.frontend.Colors;

import javafx.scene.paint.Color;

public class Colors {
    private static final Color[] colors = {
            Color.RED,
            Color.ORANGE,
            Color.LIGHTBLUE,
            Color.YELLOW,
            Color.color(0.85, 0.64, 0.58),
            Color.PURPLE,
            Color.CYAN,
            Color.MAGENTA,
            Color.PINK,
            Color.LIME,
            Color.BROWN,
            Color.GRAY,
            Color.BLUE,
            Color.LIGHTGREEN,
            Color.LIGHTPINK,
            Color.LIGHTGRAY,
            Color.DARKRED,
            Color.DARKGREEN,
            Color.DARKBLUE,
            Color.DARKORANGE,
            Color.AQUA,
            Color.BLACK,
            Color.WHITE,
            Color.SILVER,
            Color.GOLD,
            Color.INDIGO,
            Color.TEAL,
            Color.VIOLET,
            Color.YELLOWGREEN,
            Color.CRIMSON,
            Color.LAVENDER,
            Color.SALMON,
            Color.CHOCOLATE,
            Color.TURQUOISE,
            Color.CORAL,
            Color.LIGHTCORAL,
            Color.DARKCYAN,
            Color.LIGHTCYAN,
            Color.DARKVIOLET,
            Color.MEDIUMVIOLETRED,
            Color.LIGHTSALMON,
            Color.LIGHTSTEELBLUE,
            Color.DARKGOLDENROD,
            Color.DARKKHAKI,
            Color.LIGHTSEAGREEN,
            Color.DARKSLATEGRAY,        // Dark Slate Gray
            Color.DEEPPINK,             // Deep Pink
            Color.FORESTGREEN,          // Forest Green
            Color.HOTPINK,              // Hot Pink
            Color.MEDIUMBLUE,           // Medium Blue
            Color.OLIVE,                // Olive
            Color.PLUM,                 // Plum
            Color.ROYALBLUE,            // Royal Blue
            Color.SIENNA,               // Sienna
            Color.SLATEGRAY,            // Slate Gray
            Color.TOMATO,               // Tomato
            Color.WHEAT,                // Wheat
            Color.DARKOLIVEGREEN,       // Dark Olive Green
            Color.DARKORCHID,           // Dark Orchid
            Color.DARKSLATEBLUE,        // Dark Slate Blue
            Color.DIMGRAY,              // Dim Gray
            Color.LIGHTGOLDENRODYELLOW, // Light Goldenrod Yellow
            Color.LIGHTSLATEGRAY,       // Light Slate Gray
            Color.MEDIUMAQUAMARINE,     // Medium Aquamarine
            Color.MEDIUMSEAGREEN,       // Medium Sea Green
            Color.MEDIUMSLATEBLUE,      // Medium Slate Blue
            Color.MEDIUMSPRINGGREEN,    // Medium Spring Green
            Color.MEDIUMTURQUOISE,      // Medium Turquoise
            Color.MEDIUMVIOLETRED,      // Medium Violet Red
            Color.MIDNIGHTBLUE,         // Midnight Blue
            Color.OLIVEDRAB,            // Olive Drab
            Color.ORANGERED,            // Orange Red
            Color.ORCHID,               // Orchid
            Color.PALEGOLDENROD,        // Pale Goldenrod
            Color.PALEGREEN,            // Pale Green
            Color.PALETURQUOISE,        // Pale Turquoise
            Color.PALEVIOLETRED,        // Pale Violet Red
            Color.PAPAYAWHIP,           // Papaya Whip
            Color.PEACHPUFF,            // Peach Puff
            Color.PERU,                 // Peru
            Color.POWDERBLUE,           // Powder Blue
            Color.ROSYBROWN,            // Rosy Brown
            Color.SADDLEBROWN,          // Saddle Brown
            Color.SANDYBROWN,           // Sandy Brown
            Color.SEAGREEN,             // Sea Green
            Color.SEASHELL,             // Sea Shell
            Color.SKYBLUE,              // Sky Blue
            Color.SLATEBLUE,            // Slate Blue
            Color.SNOW,                 // Snow
            Color.SPRINGGREEN,          // Spring Green
            Color.STEELBLUE,            // Steel Blue
            Color.TAN,                  // Tan
            Color.THISTLE,              // Thistle
            Color.TOMATO,               // Tomato
            Color.TURQUOISE,            // Turquoise
            Color.VIOLET,               // Violet
            Color.WHEAT,                // Wheat
            Color.WHITESMOKE,           // White Smoke
            Color.YELLOWGREEN,          // Yellow Green
            Color.DARKGRAY               // Dark Gray

    };

    public static String getColor(int index) {
        if (index >= 0 && index < colors.length) {
            Color color = colors[index];
            return toRGBCode(color);
        }
        return null;
    }


    // Helper method to convert Color object to RGB code
    private static String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}


