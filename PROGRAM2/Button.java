import javax.swing.*;
import java.awt.*;

public class Button
{
    private JButton button;
    private Character[] states;
    private Character currentState;
    private Integer adjacentMines;
    private boolean isMine;


    public Button()
    {
        states = new Character[]{(char) 0, 'M', '?'};
        currentState = states[0];
        button = new JButton(currentState.toString());
        button.setMargin(new Insets(0, 0, 0, 0));
    }


    public Character[] getStates()
    {
        return states;
    }


    public int getCurrentState()
    {
        return currentState;
    }


    public void setCurrentState(Character num)
    {
        currentState = num;
    }


    public void reveal()
    {
        button.setText(getAdjacentMines().toString());
        button.setEnabled(false);
    }


    public void setText(String text)
    {
        button.setText(text);
    }


    public boolean revealed()
    {
        return currentState != 0;
    }


    public JButton getButton()
    {
        return button;
    }


    boolean isMine()
    {
        return isMine;
    }


    void setMine()
    {
        isMine = true;
    }


    void setAdjacentMines(int count)
    {
        adjacentMines = count;
    }


    Integer getAdjacentMines()
    {
        return adjacentMines;
    }
}