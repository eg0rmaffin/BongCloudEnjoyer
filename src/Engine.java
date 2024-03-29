public class Engine {

    private char[][] board;
    private boolean isWhiteTurn;

    public Engine() {
        initializeBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        this.isWhiteTurn = true;
    }

    public Engine(String fen){
        initializeBoard(fen);
    }

    public String generateFEN() {
        StringBuilder fen = new StringBuilder();
        int emptyCount = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                char piece = board[i][j];
                if (piece == ' ') {
                    emptyCount++;
                } else {
                    if (emptyCount > 0) {
                        fen.append(emptyCount);
                        emptyCount = 0;
                    }
                    fen.append(piece);
                }
            }

            if (emptyCount > 0) {
                fen.append(emptyCount);
                emptyCount = 0;
            }

            if (i < 7) {
                fen.append('/');
            }
        }

        // Дополнительные информации о ходе, цвете, рокировке и возможности взятия на проходе
        if (isWhiteTurn){
            fen.append(" w ");
        }else fen.append(" b ");
        fen.append("KQkq - "); // добавим потом логику генерации, основанную на полях класса
        fen.append("0 1"); //ааа так это возможность взятия на проходе похоже. тут и 0 3 может быть надо разобраться

        return fen.toString();
    }

    // Дополнительные методы и логика могут быть добавлены в этот класс по мере необходимости

//    private void initializeBoard() {
//        // Начальное расположение фигур на доске
//        char[][] initialBoard = {
//                {'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'},
//                {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//                {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
//                {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'}
//        };
//
//        // Копируем начальное расположение внутренний массив доски
//        board = new char[8][8];
//        for (int i = 0; i < 8; i++) {
//            System.arraycopy(initialBoard[i], 0, board[i], 0, 8);
//        }
//    }


    private void initializeBoard(String fen) {

        board = new char[8][8];
        // Разбиваем FEN строку на части
        String[] parts = fen.split(" ");

        // Расстановка фигур на доске
        String[] rows = parts[0].split("/");
        for (int i = 0; i < 8; i++) {
            String row = rows[i];
            int j = 0;
            for (char c : row.toCharArray()) {
                if (Character.isDigit(c)) {
                    // Пустые клетки
                    int count = Character.getNumericValue(c);
                    for (int k = 0; k < count; k++) {
                        board[i][j++] = ' ';
                    }
                } else {
                    // Фигуры
                    board[i][j++] = c;
                }
            }
        }

        // Установка текущего хода
        this.isWhiteTurn = parts[1].equals("w");
    }



    public void makeMove(String uciMove) {
        if (uciMove.equals("e1g1") && board[7][4] == 'K') {
            makeCastleMove(7, 4, 7, 6); // Малая рокировка белых
        } else if (uciMove.equals("e1c1") && board[7][4] == 'K') {
            makeCastleMove(7, 4, 7, 2); // Большая рокировка белых
        } else if (uciMove.equals("e8g8") && board[0][4] == 'k') {
            makeCastleMove(0, 4, 0, 6); // Малая рокировка черных
        } else if (uciMove.equals("e8c8") && board[0][4] == 'k') {
            makeCastleMove(0, 4, 0, 2); // Большая рокировка черных
        } else if (uciMove.length() == 4) { // Обычный ход
            int startX = '8' - uciMove.charAt(1);
            int startY = uciMove.charAt(0) - 'a';
            int endX = '8' - uciMove.charAt(3);
            int endY = uciMove.charAt(2) - 'a';

            char piece = board[startX][startY];
            board[endX][endY] = piece;
            board[startX][startY] = ' ';
        } else if (uciMove.length() == 5 && uciMove.charAt(4) == 'e') { // Взятие на проходе
            int startX = '8' - uciMove.charAt(1);
            int startY = uciMove.charAt(0) - 'a';
            int endX = '8' - uciMove.charAt(3);
            int endY = uciMove.charAt(2) - 'a';

            board[endX][endY] = board[startX][startY];
            board[startX][startY] = ' ';
            board[startX][endY] = ' '; // Убираем взятую пешку на проходе
        } else if (uciMove.length() == 5 && (uciMove.charAt(4) == 'q' || uciMove.charAt(4) == 'r'
                || uciMove.charAt(4) == 'b' || uciMove.charAt(4) == 'n')) { // Превращение пешки
            int startX = '8' - uciMove.charAt(1);
            int startY = uciMove.charAt(0) - 'a';
            int endX = '8' - uciMove.charAt(3);
            int endY = uciMove.charAt(2) - 'a';

            char promotionPiece = uciMove.charAt(4);
            board[endX][endY] = promotionPiece;
            board[startX][startY] = ' ';
        }

        // Переключаем очередь хода
        this.isWhiteTurn = !this.isWhiteTurn;
    }

    private void makeCastleMove(int startRow, int startCol, int endRow, int endCol) {
        char piece = board[startRow][startCol];
        board[endRow][endCol] = piece;
        board[startRow][startCol] = ' ';
    }

}
