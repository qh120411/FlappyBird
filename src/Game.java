import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Main game engine handling physics, collision detection, and game state.
 */
public class Game {

    public static final int GROUND_Y = App.HEIGHT - 80;
    public static final int CEILING_Y = -20;

    // Game timing constants
    private static final int PAUSE_DELAY = 8;
    private static final int RESTART_DELAY = 8;

    private final Difficulty difficulty;
    private final Keyboard keyboard;

    private Bird bird;
    private List<Pipe> pipes;

    private GameState state;
    private boolean quizPending;
    private boolean gameOverEventPending;

    private int pauseDelay;
    private int restartDelay;
    private int pipeDelay;

    public int score;
    private int quizzesAsked;
    private int quizzesCorrect;

    public Game(Difficulty difficulty) {
        this.difficulty = difficulty == null ? Difficulty.MEDIUM : difficulty;
        this.keyboard = Keyboard.getInstance();
        restart();
    }

    public void restart() {
        state = GameState.READY;
        quizPending = false;
        gameOverEventPending = false;

        score = 0;
        quizzesAsked = 0;
        quizzesCorrect = 0;

        pauseDelay = 0;
        restartDelay = 0;
        pipeDelay = 0;

        bird = new Bird();
        bird.setGravity(difficulty.getGravity());
        bird.setFlapVelocity(difficulty.getFlapVelocity());
        bird.setMaxFallSpeed(difficulty.getMaxFallSpeed());

        pipes = new ArrayList<Pipe>();
    }

    public void update() {
        watchForReset();

        if (state == GameState.GAME_OVER) {
            bird.update();
            clampBird();
            return;
        }

        watchForStart();
        watchForPause();

        if (state == GameState.READY || state == GameState.PAUSED || state == GameState.QUIZ) {
            return;
        }

        watchForFlap();
        bird.update();
        movePipes();
        checkForCollisions();
        clampBird();
    }

    public List<Render> getRenders() {
        List<Render> renders = new ArrayList<Render>();
        renders.add(new Render(0, 0, "lib/background.png"));
        for (Pipe pipe : pipes) {
            renders.add(pipe.getRender());
        }
        renders.add(bird.getRender());
        return renders;
    }

    public GameState getState() {
        return state;
    }

    public int getQuizzesAsked() {
        return quizzesAsked;
    }

    public int getQuizzesCorrect() {
        return quizzesCorrect;
    }

    public boolean consumeQuizTrigger() {
        boolean value = quizPending;
        quizPending = false;
        return value;
    }

    public void applyQuizResult(boolean correct) {
        if (state != GameState.QUIZ) {
            return;
        }

        if (correct) {
            quizzesCorrect++;
            score++;
        } else if (difficulty.getWrongAnswerPenalty() > 0) {
            score = Math.max(0, score - difficulty.getWrongAnswerPenalty());
        }

        state = GameState.RUNNING;
    }

    public boolean consumeGameOverEvent() {
        boolean value = gameOverEventPending;
        gameOverEventPending = false;
        return value;
    }

    private void watchForStart() {
        if (state == GameState.READY && keyboard.consumePress(KeyEvent.VK_SPACE)) {
            state = GameState.RUNNING;
            bird.flap();
        }
    }

    private void watchForFlap() {
        if (state == GameState.RUNNING && keyboard.consumePress(KeyEvent.VK_SPACE)) {
            bird.flap();
        }
    }

    private void watchForPause() {
        if (state == GameState.READY || state == GameState.QUIZ || state == GameState.GAME_OVER) {
            return;
        }

        if (pauseDelay > 0) {
            pauseDelay--;
        }

        if (pauseDelay <= 0 && keyboard.consumePress(KeyEvent.VK_P)) {
            state = state == GameState.PAUSED ? GameState.RUNNING : GameState.PAUSED;
            pauseDelay = PAUSE_DELAY;
        }
    }

    private void watchForReset() {
        if (restartDelay > 0) {
            restartDelay--;
        }

        if (restartDelay <= 0 && keyboard.consumePress(KeyEvent.VK_R)) {
            keyboard.clear();
            restart();
            restartDelay = RESTART_DELAY;
        }
    }

    private void movePipes() {
        pipeDelay--;

        if (pipeDelay < 0) {
            pipeDelay = difficulty.getPipeDelay();
            Pipe northPipe = null;
            Pipe southPipe = null;

            for (Pipe pipe : pipes) {
                if (pipe.x + pipe.width < 0) {
                    if (northPipe == null) {
                        northPipe = pipe;
                    } else if (southPipe == null) {
                        southPipe = pipe;
                        break;
                    }
                }
            }

            if (northPipe == null) {
                northPipe = new Pipe("north");
                pipes.add(northPipe);
            } else {
                northPipe.reset();
            }
            northPipe.setSpeed(difficulty.getPipeSpeed());

            if (southPipe == null) {
                southPipe = new Pipe("south");
                pipes.add(southPipe);
            } else {
                southPipe.reset();
            }
            southPipe.setSpeed(difficulty.getPipeSpeed());

            northPipe.y = southPipe.y + southPipe.height + difficulty.getPipeGap();
        }

        for (Pipe pipe : pipes) {
            pipe.update();
        }
    }

    private void checkForCollisions() {
        for (Pipe pipe : pipes) {
            if (pipe.collides(bird.x, bird.y, bird.width, bird.height)) {
                setGameOver();
                return;
            }

            if (!pipe.scored && pipe.isSouth() && pipe.x + pipe.width < bird.x) {
                pipe.scored = true;
                quizzesAsked++;
                quizPending = true;
                state = GameState.QUIZ;
                return;
            }
        }

        if (bird.y + bird.height >= GROUND_Y) {
            bird.y = GROUND_Y - bird.height;
            setGameOver();
        }
    }

    private void clampBird() {
        if (bird.y < CEILING_Y) {
            bird.y = CEILING_Y;
            bird.yVelocity = 0;
        }
    }

    private void setGameOver() {
        if (state == GameState.GAME_OVER) {
            return;
        }

        state = GameState.GAME_OVER;
        bird.dead = true;
        gameOverEventPending = true;
    }
}
