# tools for minimax

def _switch_rate(r):
    '''Return opposing players rating corresponding to r.'''

    return r * -1 # one's loss is the other's gain

class GameState:
    
    """State of a two-person game, including player about to play.

       Assumptions:
       Two-person, zero-sum game, with exactly three outcomes:
       player one can win, lose, or draw, whereas player two
       can (respectively) lose, win, or draw.
       Each ply toggles player_1 <-> player_2.
    """
    '''Class constants.'''
    WIN = 1
    LOSE = -1
    DRAW = 0
    INF = float(1e30000) 

    def __init__(self, start_state):
        """Create a new Game in starting state.

            Arguments
            start_state:  Tuple (layout, starting player).
        """
        
        self._state = start_state

    def terminal_eval(self):
        """Return current player's score at end of game.

           Assumptions:
           The game is in a state reachable from the initial position by a
           sequence of plies, with a current player to play, but no further
           plies allowed.

           Returns:
           Return whether the current player wins, loses, or ties.
        """

        if self.winner(self.player()):
            return self.WIN
        elif self.winner(self.opponent()):
            return self.LOSE
        else:
            return self.DRAW

    def heuristic_eval(self):
        """Return estimate of current player's score at end of game.

           Assumptions:
           Game is in a state reachable from initial position by sequence
           of plies, current player to play, possibly further moves allowed.

           Returns:
           Confidence that current player wins (0,1], loses [-1,0), or
           draws (0).
        """
        
        raise NotImplementedError('Implemented in GameState subclass')

    def minimax(self, foresight, pred_max=-1, layout=None):
        """Return best move and score for current GameState.

           Arguments:
           foresight: Number of plies we may look ahead.
           pred_max:  Best score so far of predecessor GameState.
           layout:    Dictionary containing best score and move
                      for each layout
           
            Assumptions:
            Current player has at least one legal move available.

            Returns (m, s)
            s:  Highest score that current player can guarantee.
            m:  Next move that allows current player to guarantee s.
        """
        
        best_move = None
        for m in self.next_move():
            g = self.make_move(m)
            score = - g.minimax_best(foresight)
            if best_move is None or score > pred_max:
                best_move = m
                pred_max = score
        return best_move, pred_max
    
    def minimax_best(self, foresight):
        '''make a tree and find the best score.'''
        
        if (not foresight) or (not self.next_move()):
            return self.terminal_eval()
     
        best_score = None
        for move in self.next_move():
            if best_score == 1:
                return 1          
            else:
                g = self.make_move(move)
                score = - g.minimax_best(foresight-1)
                if (best_score == None) or (score > best_score):
                    best_score = score
        return best_score

    def player(self):
        """Return current player --- the one with option of moving.

           Assumptions
           Player returned is one of 'p1' or 'p2'.
           Player returned might have no legal moves available.
        """

        return self._state[1]  # state is a 2-tuple 
    
    def opponent(self):
        """Return opponent of current player."""
        if self.player() == 'p1':
            return 'p2'
        else:
            return 'p1'

    def next_move(self):
        """Return a sequence of all legal moves."""
        raise NotImplementedError('Implement next_move in GameState subclass')

    def winner(self, player):
        """Return whether this player has won."""
        raise NotImplementedError('Implement winner in GameState subclass')

    def make_move(self, move):
        """Return new GameState by applying move."""
        raise NotImplementedError('Implement make_move in GameState subclass') 