#! /usr/bin/env python
#
# Copyright CORVINNO 2011
#   Author Adriaan de Groot <groot@kde.org>
#
# This file is licensed to the members of the ALERT consortium
# under the terms of the Free Software license determined for
# the ALERT project.
"""
A simple Python script that understands the default Bugzilla 
HTML template for bug activity -- this includes a table
with who, when, what, removed and added columns which is
the interesting part of the file.

Usage:
Run this script with one or more bug numers as arguments
and the KDE bugzilla will be queried, HTML parsed, and
a very basic XML output will be produced.

Instead of bug numbers, you can give filenames of previously
downloaded (e.g. with wget) output of the bug activity page.
"""

import sys
import os
import urllib2
import HTMLParser

class ActivityRow(object):
    """
    Represents a single row of the table.
    The elements of the row are stored in a single Python
    list; tags are stored as a 1-tuple of ('<tag>',) and
    data strings are stored as strings. No attempt is made
    to preserve a hierarchy.
    """
    def __init__(self, pos=None, attr=None):
        """
        Initialize a row with an optional position (in the file)
        and attributes. These are not used in the default implementation.
        """
        self.s = []
        self.pos = pos
        self.attr = attr
        
    def append(self, s):
        """
        Add an item to the row. This should either be a string
        (for data) or a 1-tuple (for tags).
        """
        self.s += [s]

    def tags(self):
        """
        Returns the number of tags in this row.
        """
        return len([x for x in self.s if isinstance(x,tuple)])

    def __len__(self):
        # Len of the row is the total number of tags and data
        # elements. This isn't particularly useful, I don't think.
        return len(self.s)
        
    def __getitem__(self, i):
        # Allow indexing into the elements of the row.
        return self.s[i]
        
        
        
    # The three show_*() methods are used to print out an
    # XML representation of the row. Which ones apply depends
    # on the context of the row and its length:
    #
    # - a 5-tag row is the start of a new display row
    #   who/when/what/removed/added; this usually has a first
    #   <td> with a rowspan= attribute to make additional
    #   changes show up "collected" with the first who/when
    #   information. For these rows, use show_start()
    #   to get the who/when information, then show_body()
    #   for the normal change information.
    # - a 3-tag row only has what/removed/added information,
    #   and is assumed to belong to the previous 5-tag row
    #   which has the who/when information. Only use show_body().
    #
    # After showing the 3-tag rows that belong with a particular
    # 5-tag row, call show_end() to close up the XML element.
    def show_start(self, dest=sys.stdout):
        """
        Shows the who/when information from a 5-tag row and
        starts an XML <change> element. The XML is written to
        the file object @p dest.
        """
        assert(self.s[0] == ('<td>', ) )
        assert(self.tags()==5)
        dest.write('<change>\n')
        count = 0
        for s in self.s:
            if isinstance(s,tuple):
                # Count the tags and use them to drive the
                # XML element tags (opening and closing)
                count += 1
                if count == 1:
                    dest.write('  <who>')
                elif count == 2:
                    dest.write('</who>\n')
                    dest.write('  <when>')
                if count > 2:
                    dest.write('</when>\n')
                    break
            else:
                # Plain string data belongs inside the elements.
                dest.write(s)
                    
    
    def show_body(self, dest=sys.stdout):
        """
        Shows the what/added/removed information from a 3- or 5-tag
        row. Writes the <what>, <removed> and <added> elements
        that are supposed to be inside a <changeitem> element.
        The XML is written to the file object @p dest.
        """
        assert(self.s[0] == ('<td>', ) )
        assert(self.tags() in [3,5])
        if self.tags() == 3:
            # Pretend we've already seen who and when
            count = 2
        else:
            # Need to skip over who and when
            count = 0
        
        dest.write('  <changeitem>\n')
        for s in self.s:
            if isinstance(s,tuple):
                count += 1
                if count < 3:
                    # Still on who and when
                    pass
                elif count == 3:
                    dest.write('    <what>')
                elif count == 4:
                    dest.write('</what>\n')
                    dest.write('    <removed>')
                elif count == 5:
                    dest.write('</removed>\n')
                    dest.write('    <added>')
            elif count < 3:
                # Any string data from who/when columns is already
                # printed by show_start(), so skip it.
                pass
            else:
                # String data in remaining columns is printed.
                dest.write(s)
        dest.write('</added>\n')
        dest.write('  </changeitem>\n')

    def show_end(self, dest=sys.stdout):
        """
        Close XML tags related to this row. Only makes sense
        if you called show_start() previously.
        """
        dest.write('</change>\n')

class BugzillaActivity(HTMLParser.HTMLParser):
    """
    A crude HTML parser that only collects <tr> elements
    inside of <table> in ActivityRow objects. Note that *every*
    row gets stuffed into an ActivityRow, and only the 
    is_interesting() call bothers to check if the table
    structure found actually matches the table we're interested
    in.
    
    """
    def __init__(self, dest=None):
        """
        Create a parser. The parser starts in the not-collecting
        state. @p dest indicates where output is to go when it
        has found a suitable activity log table. If @p dest is
        None, output goes to stdout.
        """
        HTMLParser.HTMLParser.__init__(self)
        self.collecting = False # Are we collecting everything as part of the activity table?
        self.current_row = None
        self.rows = None
        self.dest = dest
        
    def handle_starttag(self, tag, attr):
        if tag=='table':
            # New table, so start over.
            self.collecting = True
            self.current_row = None
            self.rows = []
        elif tag=='tr' and self.collecting:
            # When a row starts, start a new ActivityRow object
            self.current_row = ActivityRow(self.getpos(), attr)
            self.rows += [self.current_row]
        elif self.collecting:
            # Any other tag gets sent to the current row object
            # (presumably this is only <th> or <td>).
            self.current_row.append( ('<%s>' % tag,) )
        
    def handle_endtag(self, tag):
        if tag=='table':
            # At the  end of the table, check if it was the one
            # we are interested in and if so, print it.
            self.collecting = False
            if self.is_interesting():
                self.show(self.dest is None and sys.stdout or self.dest)
            self.rows = None
            self.current_row = None
        elif tag=='tr' and self.current_row:
            self.current_row = None
        
    def handle_data(self, data):
        # Regular data is added to the row if we're collecting one.
        if self.collecting and self.current_row:
            s = data.strip()
            if s:
                self.current_row.append(s)

    def handle_charref(self, n):
        # The @ sign is character reference escaped in KDE's bugzilla,
        # so unescape it.
        if self.collecting and self.current_row and n=='64':
            self.current_row.append('@')

    def show(self, dest=sys.stdout):
        """
        Print the table as XML. The XML goes to the given file @p dest.
        """
        if self.rows:
            # Inner or previous 5-tag line.
            inner = None
            for r in self.rows[1:]:
                # A 5-tag row indicates the start of a new change;
                # need to end the previous one if there is one.
                if r.tags()==5:
                    if inner:
                        inner.show_end(dest)
                    inner = r
                    inner.show_start(dest)
                # Every row should show the change items.
                r.show_body(dest)
            if inner:
                inner.show_end(dest)
        
    def is_interesting(self):
        """
        Check that the collected rows make sense as an activity table.
        The default structure is one row with five <th> elements
        (who/when/what/removed/added) and then rows of 5 or 3 elements
        with the actual information. Any table matching that structure
        is deemed "interesting" (ie. is probably the table we want).
        """
        if not self.rows:
            return False
            
        # The first row is 5 <th> elements
        if len(self.rows[0]) != 10:
            return False
        for i in [0,2,4,6,8]:
            if self.rows[0][i] != ('<th>',):
                return False

        # All the remaining rows have 5 columns -- for a new "who, when" row --
        # or just 3 columns for continuations.
        l = [ x.tags() in [3,5] for x in self.rows ]
        return all(l)


for a in sys.argv[1:]:
    # New activity parser for every bug or file
    p = BugzillaActivity()
    if os.path.exists(a):
        # Filenames are opened and read in one go
        p.feed(open(a).read())
    elif a.isdigit():
        # All digits suggests a bug number
        p.feed(urllib2.urlopen('http://bugs.kde.org/show_activity.cgi?id=%s' % a).read())
    else:
        print "Argument '%s' is not understood." % a
        sys.exit(1)

