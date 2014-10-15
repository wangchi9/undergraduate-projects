"""Playing Ghost."""

from lextrie import LexTrie
from player import HumanPlayer, ComputerPlayer
import random
import time
import os

class GameOver(Exception):
    """Raised to signal end of the game Ghost."""
    
    pass
    
class Ghost(object):
    """A game of Ghost. Ghost has the following attriburtes:
    -- _dictionary: a wordfile (see LextTrie for format) of all known words.
    -- _prefix: the current prefix in the game.
    -- _players: the list of Players in the game.
    """

    def __init__(self, players, path):
        """Create a new Ghost game with the dictionary from the word
        input file 'path'. The (is_human?, iq) list 'players'
        specifies the Players in this Game."""

        self._dictionary = LexTrie(path)
        self._prefix = ""
        self._players = []
        for (is_human, iq) in players:
            if is_human:
                self._players.append(HumanPlayer(path))
            else:
                lexicon = _make_lexicon(path, iq)
                self._players.append(ComputerPlayer(lexicon))
                # remove the lexicon file
                # you may want to comment this line for debugging purposes
                os.remove(lexicon)
                
    def play(self):
        """Play Ghost."""

        while(True):
            for (p, player) in enumerate(self._players):
                try:
                    self._play_round(p, player)
                except GameOver:
                    print "Game Over!"
                    return

    def _play_round(self, p, player):
        """Play one round of Ghost: player 'p' plays."""
        
        print ">>> " + self._prefix + " <<<"
        
        (challenge, next_char) = player.play(self._prefix, len(self._players))
        if challenge:
            self._play_challenge(p)
            raise GameOver

        print "Player " + str(p) + " added '" + next_char + "'."
        self._prefix += next_char
        
        data = self._dictionary.data(self._prefix)
        if data:
            print (self._prefix + "::" + data +
                   "\nPlayer " + str(p) + " loses!")
            raise GameOver
                    
    def _play_challenge(self, p):
        """Player 'p' challenges."""
        
        print "Player " + str(p) + " challenges!"
        try:
            word = self._dictionary.find_all(self._prefix)[0]
            data = self._dictionary.data(word)
            print (word + "::" + data +
                   "\nPlayer " + str(p) + " loses!\n")
        except IndexError:
            print ("No such word! Player " +
                   str((p - 1) % len(self._players)) + " loses!")

def _make_lexicon(path, iq):
    """Randomly forget (1-'iq')*100 percent of the words from the
    wordfile 'path', write them into the file, and return the file
    name."""

    # pseudo-random psudo-unique file name
    file_name = "vocab" + str(time.time()) + str(random.random()) + ".txt"
    vocab = open(file_name, 'w')
    all_words = open(path)
    random.seed()
    for word in all_words.readlines():
        if random.random() < iq:
            vocab.write(word)
    all_words.close()
    vocab.close()
    return file_name
        
if __name__ == '__main__':

    # make a game with two human players, based on the example word list
    # game = Ghost([(True, 42), (True, 42)], "short.txt")

    # play against a dumbed down computer, based on a medium word list
    game = Ghost([(True, 42), (False, 0.5)], "medium.txt")

    # play against two dumbed down computers, based on a medium word list
    #game = Ghost([(False, 0.75), (True, 42), (False, 0.5)], "medium.txt")

    # play!
    game.play()
