package cs3500.music.model;

import java.util.List;
import java.util.TreeMap;

// Created by xuanyuli on 3/1/16.

/**
 * The interface of IMusicEditorModel
 * Can be used for many different types of music editor
 */
public interface IMusicEditorModel {

  /**
   * Add a note to this model
   * @param n Note
   */
  void addNote(Note n);

  /**
   * Remove a note from this model
   * @param n Note
   * @throws IllegalArgumentException
   * if this model does not have note
   * "Cannot remove it, because it does not exist!"
   */
  void removeNode(Note n);

  /**
   * Let the input model play with the old one at
   * the same time
   * @param music IMusicEditorModel
   */
  void combineSim(IMusicEditorModel music);

  /**
   * Let the input model play with the old one,
   * one by one, it looks like:
   * old + input
   * @param music IMusicEditorModel
   */
  void combineCon(IMusicEditorModel music);

  /**
   * get all the notes in this model
   * @return List List of Note
   */
  List<Note> getAllNotes();

  /**
   * Get the treemap of this model
   * @return TreeMap (TreeMap<TimeLine, List of Note>)
   */
  TreeMap<Integer, List<Note>> getSet();

  /**
   * Change a note length by input i;
   * i < 0 make it shorter
   * i > 0 make it longer
   * @param n Note
   * @param i Integer
   * @throws IllegalArgumentException
   * user make the note out of range 0 - 10000
   * "Cannot make a note less than one!"
   * if the input n is note exist:
   * "Do not find this note!"
   */
  void modifyNote(Note n, int i);

  /**
   * Move the given note n, vertically
   * @param n Note
   * @param i Integer
   * @throws IllegalArgumentException
   * if user move the note out of range(0-128)
   * "Cannot move to that field, out range!"
   * if the input n is note exist:
   * "Do not find this note!"
   */
  void moveNoteV(Note n, int i);

  /**
   * Move the given note n, horizontally
   * @param n Note
   * @param i Integer
   * @throws IllegalArgumentException
   * if user move the note out of range
   * "Cannot move beyond 0 points
   * if the input n is note exist:
   * "Do not find this note!"
   */
  void moveNoteH(Note n, int i);
}
