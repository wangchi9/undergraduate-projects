# tic-tac-toe game
import minimax as mm
import copy

class TicTacToe(mm.GameState):

    """Tic-tac-toe GameState.
    """
        
    def __init__(self, start_state): 
        """Create a tic-tac-toe game in start_state.

           Arguments:
           start_state: A tuple consisting of two elements:
                          --a list of k lists, where each list has k entries,
                            consisting of either None (empty), 'p1' (occupied
                            by player 1), or 'p2' (occupied by player 2), where
                            k is, implicitly, the size of the list
                          --current (to play) player, 'p1' or 'p2'.

            Assumption:
            start_state represents a valid tic-tac-toe game.

           Returns:
           A tic-tac-toe game in start_state.
        """
        
        mm.GameState.__init__(self, start_state)
        r_win = [((0, 0), (0, 1), (0, 2)), ((1, 0), (1, 1), 
                                            (1, 2)), ((2, 0), (2, 1), (2, 2))]
        c_win = [((0, 0), (1, 0), (2, 0)), ((0, 1), (1, 1), 
                                            (2, 1)), ((0, 2), (1, 2), (2, 2))]
        d_win = [((0, 0), (1, 1), (2, 2)), ((0, 2), (1, 1), (2, 0))]
        self.win_set = r_win + c_win + d_win
        
    def winner(self, player):
        """Return whether player has won.

           Arguments:
           Player may be either the current player or the opponent.

           Assumptions:
           Only one of p1 or p2 may be the winner.

           Returns
           Exactly one of True or False
        """        
                
        for win in self.win_set:
            p1 = get_player(self._state[0], win[0], player)
            p2 = get_player(self._state[0], win[1], player)
            p3 = get_player(self._state[0], win[2], player)
            if p1 and p2 and p3:
                return True
        return False
        
    def __str__(self):
        '''String representation of board.
           
           Returns
           A string where the first line reads 'To play: p?\n'
           where p? is the current player,
           followed by a \n-terminated line for each row, where each
           instance of p1 is replaced by X, each p2 is replaced by O,
           and each None is replaced by -
        '''
        
        str1 = "To play: " + self._state[1] + "\n"
        str2 = ''
        for r in self._state[0]:
            for c in r:
                if c == 'p1':
                    str2 += 'X'
                elif c == 'p2':
                    str2 += 'O'
                else:
                    str2 += '-'
            str2 += '\n'
            
        return str1 + str2
              
    def next_move(self):
        """Return a (possibly empty) list of legal moves for current player.

            Assumptions:
            The list will be empty if either player has already won, and
            the order of moves in the list is not significant

            Returns:
            A list of pairs of legal moves of the form (r, c) such that
            self._state[0][r][c] is currently unoccupied and the current player
            may move by occupying it.
        """
        
        next_move = []
        if not (self.winner(self.player()) or self.winner(self.opponent())):            
            for r in range(3):
                for c in range(3):
                    if not self._state[0][r][c]:
                        next_move.append((r, c))
                    
        return next_move

    def make_move(self, move):
        """Apply move to current game.

           Arguments:
           move:  A pair (r, c) representing square self._state[0][r][c]

           Assumptions:
           (r, c) are valid coordinates for self._state[0].  If they represent
           a valid move for current player, then the current player occupies
           that position and the opponent becomes the current player.
           Otherwise self._state is left unchanged.

           Returns:
           New TicTacToe gamestate with move recorded on
           self._state[0] and current player replaced by opponent, if
           this is legal.  Otherwise return None.
        """
        
        state_list = []
        r = move[0]
        c = move[1]
        if not self._state[0][r][c]:
            for state_r in range(3):
                if state_r != r:
                    state_list.append(self._state[0][state_r])
                else:
                    state_list.append([])
                    for state_c in range(3):
                        if state_c != c:
                            state_list[r].append(self._state[0][r][state_c])
                        else:
                            state_list[r].append(self._state[1])
            new_state = (state_list, self.opponent())
            return TicTacToe(new_state)
        else:
            return None

    def heuristic_eval(self):
        """Return number of opportunities open to this player, minus those
           open to opponent.  A row, column, or diagonal is an open
           opportunity if it has no squares occupied by this player's
           opponent.

           Assumptions:
           The evaluation is not exact, and is, in general, inferior to an
           exact evaluation.

           Returns:
           Number of rows, columns, diagonals open to this player, minus
           those open to opponent, divided by total possible winning lines.
        """
        
        def make_full(state, player):
            '''make the empty spaces of state full of player. return the 
            new_state'''
            new_state = [[], [], []]
            for r in range(3):
                for c in range(3):
                    if state[r][c] is None:
                        new_state[r].append(player)
                    else:
                        new_state[r].append(state[r][c])
            return new_state
        
        chance = 0
        player_state = make_full(self._state[0], self.player())
        for opp in self.win_set:
            p1 = get_player(player_state, opp[0], self.player())
            p2 = get_player(player_state, opp[1], self.player())
            p3 = get_player(player_state, opp[2], self.player())
            if p1 and p2 and p3:
                chance += 1
        
        opp_state = make_full(self._state[0], self.opponent())
        for opp in self.win_set:
            p1 = get_player(opp_state, opp[0], self.opponent())
            p2 = get_player(opp_state, opp[1], self.opponent())
            p3 = get_player(opp_state, opp[2], self.opponent())
            if p1 and p2 and p3:
                chance -= 1  
                
        return chance / 8.0 
        
    def step_left(self):
        '''return the number of None in game.'''
        
        i = 0
        for r in self._state[0]:
            for c in r:
                if c is None:
                    i += 1
        return i 
    
    def is_full(self):
        '''return True if state is full.'''
        
        for r in range(3):
            for c in range(3):
                if self._state[0][r][c] is None:
                    return False
        return True
    
# help function for winner:
def get_player(state, position, player):
    '''return Ture if the player is in the position.'''
    
    r = position[0]
    c = position[1]
    if state[r][c] == player:
        return True
    else:
        return False  