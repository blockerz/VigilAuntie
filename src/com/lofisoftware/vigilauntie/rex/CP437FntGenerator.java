package com.lofisoftware.vigilauntie.rex;

import java.io.File;
import java.io.PrintWriter;


public class CP437FntGenerator {

    // fntFileName - Name of the fnt file you want to generate minus extension - assumes png file is same name
    // Also make sure that the background of the png file is set to transparent if you want (I used white font on black background with black set transparent).
    public static StringBuffer generatecp437fnt(String fntFileName, int sizeX, int sizeY) {
        StringBuffer sb = new StringBuffer();

        sb.append("info face=\"" + fntFileName + "\" size=-1 bold=0 italic=0 charset=\"utf-8\" unicode=0 stretchH=100 smooth=1 aa=2 padding=0,0,0,0 spacing=1,1 outline=0\n" +
                "common lineHeight=12 base=8 scaleW=128 scaleH=192 pages=1 packed=0 alphaChnl=0redChnl=4 greenChnl=4 blueChnl=4\n" +
                "page id=0 file=\"" + fntFileName + ".png\"\n" +
                "chars count=256\n\n");

        int y = 0;

        for (int i = 0; i < 256; i++) {

            if (i%16 == 0 && i != 0)
                y += sizeY;

            sb.append("char id=" + i + "   x=" + sizeX*(i%16) + "   y=" + y + "   width=" + sizeX + "    height=" + sizeY + "    xoffset=0     yoffset=0     xadvance=" + sizeX + "    page=0  chnl=15\n");

        }

        return sb;
    }

    public static boolean writeFile(File out, StringBuffer sb) {

        if (!(out.exists() && out.canWrite()))
            return false;

        try {
            PrintWriter printer = new PrintWriter(out);

            printer.write(sb.toString());
            printer.flush();
            printer.close();
        }
        catch (Exception e) {
            //e.printStackTrace();
            return false;
        }

        return true;
    }

}
