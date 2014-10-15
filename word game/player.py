"""Players in the game Ghost. A Player must support the following methods:

-- words(prefix): return a list of all words that begin with prefix
'prefix' that this player knows.

-- play(prefix, num_players): return the next move of this player in
the game. The move is a tuple (challenge, next_char), where
'challenge' is True if the player decides to challenge and False
otherwise, and 'next_char' is the character the player decides to
append to the game prefix.
"""

from lextrie import LexTrie
from gametrie import GameTrie

class Player(object):
    """A player in the game Ghost. Player has the following attributes:
    -- _lexicon: a LexTrie; the words that this Player knows, with
    their definitions.
    -- _gameTrie: a GameTrie; this Player's mental image of the game,
    built at the time the game prefix was '_prefix'.
    -- _prefix: this was the game prefix string at the time the
    _game_trie was built.
    """

    def __init__(self, path):
        """Create a new Player whose lexicon is based on the
        vocabulary from the input word file 'path'.
        -- path: a string; the path to an input file; see LexTrie for
        the format of the input file."""

        self._lexicon = LexTrie(path)
        self._game_trie = None
        self._prefix = ''
        
    def words(self, prefix):
        """Return the list of all words which begin with string
        'prefix' that this Player knows."""

        return self._lexicon.find_all(prefix)

class HumanPlayer(Player):
    """A human player in the game Ghost."""

    def cheat(self, prefix):
        """Cheat: print all words that begin with 'prefix'."""

        print self.words(prefix)

    def play(self, prefix, num_players):
        """Return the tuple (challenge?, the character to append to
        string 'prefix') in the game with 'num_players' players. This
        variant ignores the lexicon and prompts the user for the
        decision."""

        cheat = raw_input("Cheat? (y/N)")
        if cheat == 'y':
            self.cheat(prefix)
            
        if prefix:
            challenge = raw_input("Challenge? (y/N)")
            if challenge == "y":
                return (True, "")
            
        return (False, raw_input("Next character: "))

class ComputerPlayer(Player):
    """A computer player in the game Ghost."""

    def play(self, prefix, num_players):
        """Return the tuple (challenge?, the character to append to
        string 'prefix') in the current game."""

        # get to the node in my lexicon trie that corresponds to
        # prefix 'prefix'
        prefix_node = self._lexicon.find_node(prefix)

        # if I do not know a word that begins with 'prefix',
        # challenge -- nothing to lose!
        if (not prefix_node) or prefix_node.is_leaf():
            return (True, '')

        # if I don't yet have a game trie, make one
        if not self._game_trie:
            self._prefix = prefix
            self._game_trie = GameTrie(prefix_node, num_players)
            
        # find where I am in my game trie
        current_state = self._game_trie.find_node(prefix[len(self._prefix):])

        # if there is a winning child, return its key
        # otherwise, if there is a child which does not
        # immediately lose, return its key
        # otherwise, any child's key would do
        (nexts, nxt) = (current_state.children(), None)
        for key in nexts:
            if self._game_trie.is_winning(nexts[key]):
                return (False, key)
            elif not nxt or not self._lexicon.is_word(prefix + key):
                nxt = key

        return (False, nxt)
