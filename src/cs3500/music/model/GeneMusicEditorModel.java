package cs3500.music.model;

import java.util.TreeMap;
import java.util.*;
import static cs3500.music.model.Pitch.pitchToString;


// Created by xuanyuli on 3/1/16.

/**
 * To represent a class of GeneMusicEditorModel
 */
public class GeneMusicEditorModel implements IMusicEditorModel {

  // fields of the class
  int lowSide = 131;
  int highSide = 0;
  int lastSide = 0;
  private TreeMap<Integer, List<Note>> set = new TreeMap<>();

  /**
   * Constructor
   * @param t List of Note
   */
  public GeneMusicEditorModel(List<Note> t) {
    t.forEach(this::addNote);
  }

  /**
   * update the size of the music editor
   * @param n Note
   */
  private void newSize(Note n) {
    this.lowSide = Math.min(n.getPosition(), this.low());
    this.highSide = Math.max(n.getPosition(), this.high());
    this.lastSide = Math.max(n.getStart() + n.getLength(), this.last());
  }

  /**
   * get the low side of the music editor
   * @return int
   */
  public int low() {
    return this.lowSide;
  }


  /**
   * get the high side of the music editor
   * @return int
   */
  public int high() {
    return this.highSide;
  }


  /**
   * get the last side of the music editor
   * @return int
   */
  public int last() {
    return this.lastSide;
  }

  /**
   * Get the treemap of this model
   * @return TreeMap (TreeMap<TimeLine, List of Note>)
   */
  public TreeMap<Integer, List<Note>> getSet() {
    return this.set;
  }


  @Override
  public void addNote(Note n) {
    if (set.get(n.getStart()) == null) {
      set.put(n.getStart(), new ArrayList<>(Collections.singletonList(n)));
    } else {
      set.get(n.getStart()).add(n);
    }
    this.newSize(n);
  }

  @Override
  public void removeNode(Note n) {
    List<Note> result = this.set.get(n.getStart());
      if (result != null &&result.contains(n)) {
        result.remove(n);
        this.set.replace(n.getStart(), result);

        this.lowSide = 131;
        this.highSide = 0;
        this.lastSide = 0;
        this.getAllNotes().forEach(this::newSize);

      } else {
        throw new IllegalArgumentException(
                "Cannot remove it, because it does not exist!");

    }
  }


  @Override
  public void combineSim(IMusicEditorModel music) {
    List<Note> lon = music.getAllNotes();
    lon.forEach(this::addNote);
  }

  @Override
  public void combineCon(IMusicEditorModel music) {
    List<Note> lon = music.getAllNotes();
    int i = this.last();
    for (Note n : lon) {
      n.updateStart(i);
      this.addNote(n);
    }
  }

  @Override
  public void moveNoteH(Note n, int i) {
    if (n.getStart() < -i) {
      throw new IllegalArgumentException("Cannot move beyond 0 points");
    }
    if(!this.getAllNotes().contains(n)){
      throw new IllegalArgumentException("Do not find this note!");
    }
    else {
      this.removeNode(n);
      n.updateStart(i);
      this.addNote(n);
    }
  }

  @Override
  public void moveNoteV(Note n, int i) {
    if ((n.getPosition() + i) < 0 || ((n.getPosition()) + i) > 127) {
      throw new IllegalArgumentException("Cannot move to that field, out range!");
    }
    else if (!this.getAllNotes().contains(n)) {
      throw new IllegalArgumentException("Do not find this note!");
    }
    this.removeNode(n);
    n.updatePosition(i);
    this.addNote(n);
  }

  @Override
  public void modifyNote(Note n, int i) {
    if ((n.getLength() + i < 0) || n.getLength() + 1 > 9999) {
      throw new IllegalArgumentException("Cannot make a note less than one!");
    } if (!this.getAllNotes().contains(n)) {
      throw new IllegalArgumentException("Do not find this note!");
    }
    this.removeNode(n);
    n.updateLength(i);
    this.addNote(n);
  }


  @Override
  public List<Note> getAllNotes() {
    List<Note> result = new ArrayList<>(Collections.emptyList());
    set.values().forEach(result::addAll);
    return result;
  }

  /**
   * Print the model into string
   * eg."   E4
   *    "0  X
        "1  |
   * @return String
   */
  public String modelToString() {
    String result = "";
    int sizeOfIndex = (Integer.toString(this.lastSide)).length();
    int wide = (this.highSide - this.lowSide + 1) * 5;
    if (wide <= 0) {
      wide = 0;
    }

    // the head part
    String emptyLine = new String(new char[wide]).replace("\0", " ");
    result = result + new String(new char[sizeOfIndex]).replace("\0", " ");
    for (int i = lowSide; i <= highSide; i++) {
      String s = pitchToString(i % 12) + Integer.toString(i / 12);
      int x = s.length();
      String title = null;
      if (x == 2) {
        title = "  " + s + " ";
      }
      if (x == 3) {
        title = " " + s + " ";
      }

      if (x == 4) {
        title = " " + s;
      }
      result = result + title;
    }
    result = result + "\n";

    // the body part
    for (int i = 0; i < this.lastSide; i++) {
      if (sizeOfIndex == 1) {
        result = result + String.format("%d", i);
      }
      if (sizeOfIndex == 2) {
        result = result + String.format("%2d", i);
      }
      if (sizeOfIndex == 3) {
        result = result + String.format("%3d", i);
      }
      if (sizeOfIndex == 4) {
        result = result + String.format("%4d", i);
      }
      result = result + emptyLine +"\n";

    }

    // replace by "x" and "|";
    for(Note n: this.getAllNotes()){
      char[] myresult = result.toCharArray();
      myresult[(wide + sizeOfIndex + 1) * (n.getStart() + 1)
              + sizeOfIndex + (n.getPosition() - this.lowSide) * 5 + 2] = 'X';
      for(int m = 1; m < n.getLength(); m++){
        myresult[(wide + sizeOfIndex + 1) * (n.getStart() + 1 + m) +
                sizeOfIndex + (n.getPosition() - this.lowSide) * 5 + 2] = '|';
      }
      result = String.valueOf(myresult);
    }
    return result;
  }
}





