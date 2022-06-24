package logic;

public class Mathematics {
    /**
     * Controleert of een cirkel overlapt met een lijnstuk. Met deze methode kan de botsing tussen een cirkel en een lijn gedetecteerd worden.
     * Bron: https://stackoverflow.com/questions/30559799/function-for-finding-the-distance-between-a-point-and-an-edge-in-java
     * @param x1 De x-coördinaat van het eerste punt van het lijnstuk
     * @param y1 De y-coördinaat van het eerste punt van het lijnstuk
     * @param x2 De x-coördinaat van het tweede punt van het lijnstuk
     * @param y2 De y-coördinaat van het tweede punt van het lijnstuk
     * @param xc De x-coördinaat van de cirkel
     * @param yc De y-coördinaat van de cirkel
     * @param straal De straal van de cirkel
     * @return true als er een overlap is.
     */
    public static boolean cirkelOverlaptMetLijnstuk(int x1, int y1, int x2, int y2, int xc, int yc, int straal) {
        float A = xc - x1;
        float B = yc - y1;
        float C = x2 - x1;
        float D = y2 - y1;

        float dot = A * C + B * D;
        float len_sq = C * C + D * D;
        float param = -1;
        if (len_sq != 0) //in case of 0 length line
            param = dot / len_sq;

        float xx, yy;

        if (param < 0) {
            xx = x1;
            yy = y1;
        }
        else if (param > 1) {
            xx = x2;
            yy = y2;
        }
        else {
            xx = x1 + param * C;
            yy = y1 + param * D;
        }

        float dx = xc - xx;
        float dy = yc - yy;

        return dx * dx + dy * dy <= Math.pow(straal , 2);
    }

    /**
     * Controleert of een cirkel overlapt met de rand van een rechthoek. Met deze methode kan de botsing tussen een cirkel en de randen van een rechthoek gedetecteerd worden.
     * @param xr De x-coördinaat van het eerste punt van de rechthoek
     * @param yr De y-coördinaat van het eerste punt van de rechthoek
     * @param br De breedte van de rechthoek
     * @param hr De hoogte van de rechthoek
     * @param xc De x-coördinaat van de cirkel
     * @param yc De y-coördinaat van de cirkel
     * @param straal De straal van de cirkel
     * @return true als er een overlap is.
     */
    public static boolean cirkelOverlaptMetRechthoek(int xr, int yr, int br, int hr, int xc, int yc, int straal){
        //TODO: overloop de 4 randen van de rechthoek en kijk of je overlapt
        boolean lijnstuk1 = cirkelOverlaptMetLijnstuk(xr, yr, xr, (yr + hr), xc, yc, straal);
        boolean lijnstuk2 = cirkelOverlaptMetLijnstuk(xr, yr, (xr + br), yr, xc, yc, straal);
        boolean lijnstuk3 = cirkelOverlaptMetLijnstuk(xr, (yr + hr), (xr + br), (yr + hr), xc, yc, straal);
        boolean lijnstuk4 = cirkelOverlaptMetLijnstuk((xr + br), yr, (xr + br), (yr + hr), xc, yc, straal);
        return lijnstuk1 || lijnstuk2 || lijnstuk3 || lijnstuk4;
    }

    public static boolean cirkelOverlaptMetCirkel(int x1, int y1, int x2, int y2, int r1, int r2){
        int distSq = (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
        int radSumSq = (r1 + r2) * (r1 + r2);
        if (distSq == radSumSq)
            return true;
        else if (distSq > radSumSq)
            return false;
        else
            return true;

    }


}
