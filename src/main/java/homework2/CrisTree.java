package homework2;

class CrisTree {
    public static void main(String[] args) {
        int rowCount = 10;
        String space ;
        for (int i = 1; i <= rowCount  ; i++) {
            space = "";
            for (int j = 0; j < rowCount + i  -1    ; j++) {
                if (   (rowCount - i)    >j    ){
                    space += " ";
                }
                else {
                    space += "*";

                }
            }
            System.out.println(space );
        }
    }
}
