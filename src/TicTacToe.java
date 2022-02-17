import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class TicTacToe implements ActionListener {

    // initialize game specific variables
    String player = "X";
    String computer = "O";

    String Instructions = "Instructions: \n" +
            "Get consecutive three " + player + " in a row or column or diagonal to win";

    int[][] combinations = {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},
            {0, 4, 8},
            {2, 4, 6}
    };
    int total_turns = 0;

    // initialize GUI specific variables
    Random random = new Random();
    JFrame frame = new JFrame();
    JPanel title_panel = new JPanel();
    JPanel button_panel = new JPanel();
    JLabel textField = new JLabel();
    Tile[] buttons = new Tile[9];

    public TicTacToe() {

        // create GUI
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.getContentPane().setBackground(new Color(50, 50, 50));
        frame.setLayout(new BorderLayout());

        // customize text field
        textField.setBackground(new Color(25, 25, 25));
        textField.setForeground(new Color(25, 255, 0));
        textField.setFont(new Font("Ink Free", Font.BOLD, 75));
        textField.setHorizontalAlignment(JLabel.CENTER);
        textField.setText("Tic Tac Toe");
        textField.setOpaque(true);

        // customize title field
        title_panel.setLayout(new BorderLayout());
        title_panel.setBounds(0, 0, 800, 100);
        title_panel.add(textField);

        // add tiles and set them up
        button_panel.setLayout(new GridLayout(3, 3));
        button_panel.setBackground(new Color(150, 150, 150));
        for (int i = 0; i < 9; i++) {

            buttons[i] = new Tile();
            button_panel.add(buttons[i]);
            buttons[i].addActionListener(this);

        }

        // add everything to frame
        frame.add(title_panel, BorderLayout.NORTH);
        frame.add(button_panel);

        // make frame visible
        frame.setVisible(true);

        // show instructions
        JOptionPane.showMessageDialog(null, Instructions);

        // start game
        this.startGame();

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // check which button is pressed and if move can be made on that button make the move

        for (int i = 0; i < 9; i++) {
            // check which button launched event
            if (e.getSource() == buttons[i]) {
                // check if move can be made
                if (buttons[i].getText().equals("")) {
                    // make move
                    buttons[i].setForeground(new Color(255, 0, 0));
                    buttons[i].setText(this.player);
                    if (++total_turns == 9) {
                        setEnabled(false);
                        textField.setText("Draw");
                        return;
                    }
                    if (this.check()) {
                        return;
                    }

                    // invoke pc's turn
                    textField.setText("Pc's turn");
                    setEnabled(false);
                    pcTurn();
                    setEnabled(true);
                    this.check();
                    if (++total_turns == 9) {
                        setEnabled(false);
                        textField.setText("Draw");
                        return;
                    }
                }

            }
        }

    }

    public void startGame() {
        // on random decide who's going to take first turn
        if (this.random.nextInt(2) == 0) {
            this.textField.setText("Your turn");
        } else {
            this.textField.setText("Pc's turn");
            setEnabled(false);
            pcTurn();
            total_turns++;
            setEnabled(true);
        }
    }

    public boolean check() {

        // check if someone won by checking all winning combinations
        for (int i = 0; i < combinations.length; i++) {
            // iterate over all winning combinations to check if someone won
            if (buttons[combinations[i][0]].getText().equals(buttons[combinations[i][1]].getText()) &&
                    buttons[combinations[i][0]].getText().equals(buttons[combinations[i][2]].getText())) {

                // check who won
                if (buttons[combinations[i][0]].getText().equals(this.player)) {
                    textField.setText("You Won");
                    setEnabled(false);
                    return true;
                } else if (buttons[combinations[i][0]].getText().equals(this.computer)) {
                    textField.setText("You lose");
                    setEnabled(false);
                    return true;
                }
            }
        }
        return false;
    }


    public void setEnabled(boolean enabled) {
        // set tile activation status
        for (Tile button : buttons) {
            button.setEnabled(enabled);
        }
    }


    public void pcTurn() {
        // AI working:
        // try to win, if you can't do that
        // try not to lose if you can't do that
        // make a random move
        if (!tryToWin()) {
            if (!preventLoss()) {
                makeMove();
            }
        }

    }

    public boolean preventLoss() {
        // prevent loss by checking if player can make win in next move and make move accordingly
        int count = 0;
        for (int i = 0; i < combinations.length; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[combinations[i][j]].getText().equals(player)) {
                    count++;
                }
            }
            if (count == 2) {
                for (int j = 0; j < 3; j++) {
                    if (buttons[combinations[i][j]].getText().equals("")) {
                        pcMove(buttons[combinations[i][j]]);
                        return true;
                    }
                }
            }
            count = 0;
        }
        return false;
    }

    public void makeMove() {
        // make a random move
        int r = 0;
        while (true) {
            r = random.nextInt(9);
            if (buttons[r].getText().equals("")) {
                pcMove(buttons[r]);
                return;
            }

        }
    }

    public boolean tryToWin() {
        // try to win by checking if you can get tree O in a winning combination
        int count = 0;
        for (int i = 0; i < combinations.length; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[combinations[i][j]].getText().equals(computer)) {
                    count++;
                }
            }
            if (count == 2) {
                for (int j = 0; j < 3; j++) {
                    if (buttons[combinations[i][j]].getText().equals("")) {
                        pcMove(buttons[combinations[i][j]]);
                        return true;
                    }
                }
            }
            count = 0;
        }
        return false;
    }


    public void pcMove(Tile tile) {
        // pc move settings
        if (tile.getText().equals("")) {
            tile.setForeground(new Color(0, 0, 255));
            tile.setText(this.computer);
            textField.setText("Your turn");
        }
    }

}
