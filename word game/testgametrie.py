"""Test module GameTrie."""

import unittest
from lextrie import LexTrie
from gametrie import GameTrie

class SingleTestCase(unittest.TestCase):
    """Game trie based on a single one-letter word lex trie, root is
    not winning."""

    def setUp(self):
        """Make a single one-letter word lex trie."""
        
        self.lextrie = LexTrie()
        self.lextrie.insert("a", "AAA")
        self.gametrie = GameTrie(self.lextrie._root, 2)
        
    def tearDown(self):
        """Clean up."""
        
        self.lextrie = None
        self.gametrie = None

    def test(self):
        """Test: the root of this trie should not be winning."""
        
        win = self.gametrie.is_winning(self.gametrie._root)
        assert not win,\
               "root should not be winning!"

class SimpleTestCase(unittest.TestCase):
    """Game trie based on a very simple lex trie, root is winning."""

    def setUp(self):
        """Make a simle lex trie."""

        self.lextrie = LexTrie()
        self.lextrie.insert("a", "AAA")
        self.lextrie.insert("bb", "BBB")
        self.gametrie = GameTrie(self.lextrie._root, 2)
        
    def tearDown(self):
        """Clean up."""
        
        self.lextrie = None
        self.gametrie = None

    def test(self):
        """Test: the root of this trie should be winning."""

        win = self.gametrie.is_winning(self.gametrie._root)
        assert win,\
               "root should be winning!"

class LargerTestCase(unittest.TestCase):
    """Set up for larger test cases."""
    
    def setUp(self, num_players):
        """Make a lex and game tries for a game with 'num_players'
        players. The words are the same as in the file short.txt."""

        self.lextrie = LexTrie()
        self.lextrie.insert("cool", "Cool's Data")
        self.lextrie.insert("coo", "Coo's Data")
        self.lextrie.insert("bar", "Bar's Data")
        self.lextrie.insert("cooler", "Cooler's Data")
        self.lextrie.insert("baristas", "Baristas's Data")
        self.lextrie.insert("cook", "Cook's Data")
        self.lextrie.insert("banner", "Banner's Data")
        self.lextrie.insert("bag", "Bag's Data")
        self.lextrie.insert("bank", "Bank's Data")
        self.gametrie = GameTrie(self.lextrie._root, num_players)
        
    def tearDown(self):
        """Clean up."""
        
        self.lextrie = None
        self.gametrie = None

    def winning(self, word):
        """Return whether playing 'word' results in a winning position."""
        
        return self.gametrie.is_winning(self.gametrie.find_node(word))

class LargerTestCaseTwoPlayers(LargerTestCase):
    """Test on a larger trie, two players."""

    def setUp(self):
        """Set up the game with 2 players."""
        
        LargerTestCase.setUp(self, 2)
        
    def testAllWinning(self):
        """Check that all strings that should be winning are winning."""
        
        allWinStrings = ["", "cool", "coole", "cooler", "cook",
                         "b", "ba", "ban", "bank", "bann", "banne", "banner",
                         "bari", "baris", "barist", "barista", "baristas"]
        errors = [prefx for prefx in allWinStrings if not self.winning(prefx)]
        assert not errors,\
               ("the following are incorrectly marked as not winning: " +
                str(errors))

    def testAllLosing(self):
        """Check that all strings that should not be winning are not
        winning."""

        allLoseStrings = ["c", "co", "coo", "bag", "bar"]
        errors = [prefix for prefix in allLoseStrings if self.winning(prefix)]
        assert not errors,\
               ("the following are incorrectly marked as winning: " +
                str(errors))

def single_suite():
    """Return a test suite for a single word trie, as above."""
    
    return unittest.TestLoader().loadTestsFromTestCase(SingleTestCase)

def simple_suite():
    """Return a test suite for a simple trie, as above."""

    return unittest.TestLoader().loadTestsFromTestCase(SimpleTestCase)

def larger_suite_2():
    """Return a larger test suite, as above, with 2 players."""

    return unittest.TestLoader().loadTestsFromTestCase(
        LargerTestCaseTwoPlayers)

if __name__ == '__main__':
    """Go!"""

    runner = unittest.TextTestRunner(verbosity=2)
    runner.run(single_suite())
    runner.run(simple_suite())
    runner.run(larger_suite_2())
