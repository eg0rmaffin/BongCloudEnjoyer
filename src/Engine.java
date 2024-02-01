public class Engine {

    private char[][] board;
    private boolean isWhiteTurn;

    public Engine() {
        initializeBoard();
        this.isWhiteTurn = true;
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
        fen.append(" w KQkq - ");
        if (this.isWhiteTurn){
            fen.append("0 1");
        } else {
            fen.append("1 0");
        }

        return fen.toString();
    }

    // Дополнительные методы и логика могут быть добавлены в этот класс по мере необходимости

    private void initializeBoard() {
        // Начальное расположение фигур на доске
        char[][] initialBoard = {
                {'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'},
                {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
                {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'}
        };

        // Копируем начальное расположение внутренний массив доски
        board = new char[8][8];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(initialBoard[i], 0, board[i], 0, 8);
        }
    }

    public void makeMove(String uciMove) {
        if (uciMove.equals("e1g1") && board[7][4] == 'K') { // Малая рокировка белых
            board[7][6] = 'K'; // Король на свою новую позицию
            board[7][5] = 'R'; // Ладья на свою новую позицию
            board[7][4] = board[7][7] = ' '; // Очистка старых позиций
        } else if (uciMove.equals("e1c1") && board[7][4] == 'K') { // Большая рокировка белых
            board[7][2] = 'K';
            board[7][3] = 'R';
            board[7][4] = board[7][0] = ' ';
        } else if (uciMove.equals("e8g8") && board[0][4] == 'k') { // Малая рокировка черных
            board[0][6] = 'k';
            board[0][5] = 'r';
            board[0][4] = board[0][7] = ' ';
        } else if (uciMove.equals("e8c8") && board[0][4] == 'k') { // Большая рокировка черных
            board[0][2] = 'k';
            board[0][3] = 'r';
            board[0][4] = board[0][0] = ' ';
        } else {
            int startX = '8' - uciMove.charAt(1);
            int startY = uciMove.charAt(0) - 'a';
            int endX = '8' - uciMove.charAt(3);
            int endY = uciMove.charAt(2) - 'a';

            char piece = board[startX][startY];
            board[endX][endY] = piece;
            board[startX][startY] = ' ';
        }

        // Переключаем очередь хода
        this.isWhiteTurn = !this.isWhiteTurn;
    }
}
