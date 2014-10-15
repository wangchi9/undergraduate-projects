"""Test module LexTrie -- incomplete!."""

import unittest
from lextrie import LexTrie
from testtrie import TrieEmptyTestCase, TrieSingleTestCase, TrieSimpleTestCase

class LexTrieEmptyTestCase(TrieEmptyTestCase):
    '''Test retrieving from empty Trie.'''
    
    def setUp(self):
        """Set up an empty trie."""
        
        self.trie = LexTrie()
        
    def testFindAll(self):
        """Test the find_all() method."""
        
        assert self.trie.find_all("coo") == [], \
               'found words in an empty tree!'

class LexTrieSingleTestCase(TrieSingleTestCase):
    '''Test inserting a single word into an empty Trie and
    retrieving it.'''

    def setUp(self):
        """Set up a single word trie."""
        
        self.trie = LexTrie()
        self.populate()
        
    def testFindAllWord(self):
        """Test the find_all() method with valid word."""
        
        found = self.trie.find_all("coo")
        assert found == ["coo"],\
               "found " + str(found) + ", but expected [coo]"

class LexTrieSimpleTestCase(TrieSimpleTestCase):
    '''Test inserting several words, with some common prefixes, into
    a Trie and retrieving them.'''

    def setUp(self):
        """Set up a simple trie. The words are the same as in the file
        short.txt."""
        
        self.trie = LexTrie()
        self.populate()
        
    def testFindAll0(self):
        """Test the find_all() method with valid one-letter prefix."""
        
        found = self.trie.find_all("c")
        found.sort()
        assert found == ["coo", "cook", "cool", "cooler"],\
               ("Found " + str(found) +
                ", but expected some permulation of [coo,cook,cool,cooler]")

    def testFindAll1(self):
        """Test the find_all() method with valid word."""

        found = self.trie.find_all("bar")
        found.sort()
        assert found == ["bar", "baristas"],\
               ("Found " + str(found) +
                ", but expected some permulation of [bar,baristas]")

def empty_suite():
    """Return a test suite for an empty trie."""
    
    return unittest.TestLoader().loadTestsFromTestCase(LexTrieEmptyTestCase)

def single_suite():
    """Return a test suite for a single-word trie."""

    return unittest.TestLoader().loadTestsFromTestCase(LexTrieSingleTestCase)

def simple_suite():
    """Return a test suite for a larger, simple trie."""

    return unittest.TestLoader().loadTestsFromTestCase(LexTrieSimpleTestCase)

if __name__ == '__main__':
    """Go!"""
    
    runner = unittest.TextTestRunner(verbosity=2)
    runner.run(empty_suite())
    runner.run(single_suite())
    runner.run(simple_suite())
