public class Main {

    public static void main(String[] args) {
	// write your code here
//        Engine engine = new Engine();
//        System.out.println(engine.generateFEN());
//        engine.makeMove("e2e4");
//        System.out.println(engine.generateFEN());

        Engine engine = new Engine("rnbqkbnr/1pp1pppp/p7/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 3");
        System.out.println(engine.generateFEN());
        engine.makeMove("e5d6");
        System.out.println(engine.generateFEN());
    }
}
