"""A lexicon trie.

To create a non-empty lexicon Trie, the user needs to supply either a
list of tuples (word, data), or a path to an input file. The format of
the input file is:

<word0>::<word0's definition>
<word1>::<word1's definition>
...

<wordi's definition> then becomes the data field of a TrieNode that
corresponds to the string <wordi>.

LexTrie supports the following operations (in addition to those of Trie):

-- is_word(word): return whether 'word' is a valid word in this Trie.
-- find_all(prefix): return a list of (valid) words with prefix 'prefix'.
"""

from trie import Trie, TrieNode

class LexTrie(Trie):
    """A lexicon Trie. LexTrie has the following attributes:
     -- _root: a TrieNode; the root of this Trie.
    """

    def __init__(self, words=None):
        """Create a Trie from 'words'. If 'words' is not specified,
        create an empty Trie.

        -- words: either a (string) file path or a (word, data) list.
        """

        Trie.__init__(self)
        if words:
            if isinstance(words, str):
                self._read_words(words)
            else:
                for (word, data) in words:
                    self.insert(word, data)
                    
    def is_word(self, word):
        """Return True if 'word' is a valid word and False otherwise."""

        return self.data(word) is not None

    def find_all(self, prefix):
        """Return a list of valid words with prefix 'prefix' in this
        Trie.

        -- prefix: a string; all valid words from this Trie that begin
        with 'prefix' are returned."""
        if self._root._children:
            words = _find_all(self._root, prefix)
            for i in range(len(words)):
                words[i] = prefix + words[i]
            return words
        else:
            return []
    
    def _read_words(self, path):
        """Insert every word from the word input file 'path' into this
        Trie.

        -- path: a string; a full path to the input file."""

        word_file = open(path)
        for line in word_file.readlines():
            pair = line.split('::')
            self.insert(pair[0], pair[1].rstrip())
        word_file.close()

def _find_all(root, prefix):
    '''Return a list of valid words with prefix 'prefix' in this
        Trie.

        -- prefix: a string; all valid words from this Trie that begin
        with 'prefix' are returned.'''
    if len(prefix) == 0:
        return _find_all2(root)
    else:
        return _find_all(root._children[prefix[0]], prefix[1:])
    
    
def _find_all2(root, preword=''):

    words = []
    if root._children:
        for child in root._children.keys():
            words += _find_all2(root._children[child], preword+child)
        if root._data:
            words.append(preword)
    else:
        return [preword]
    return words 