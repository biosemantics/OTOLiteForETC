package edu.arizona.sirls.client.view.to_ontologies;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.arizona.sirls.client.presenter.to_ontologies.ToOntologyPresenter;

public class ToOntologyView extends Composite implements
		ToOntologyPresenter.Display {
	/**
	 * things on the left list part
	 */
	private int regularStructureCount = 0;
	private int regularCharacterCount = 0;
	private int removedStructureCount = 0;
	private int removedCharacterCount = 0;

	private DisclosurePanel regularStrucureDisclosure;
	private DisclosurePanel regularCharacterDisclosure;
	private DisclosurePanel removedStructureDisclosure;
	private DisclosurePanel removedCharacterDisclosure;

	private VerticalPanel regularStructureList;
	private VerticalPanel regularCharacterList;
	private VerticalPanel removedStructureList;
	private VerticalPanel removedCharacterList;

	private Image updateMatchingStatusBtn;

	/**
	 * things in the middle part: matches and submissions
	 */
	private SimplePanel middlePanel;
	private SimplePanel rightPanel;

	public ToOntologyView() {
		// the giant container
		FlexTable layout = new FlexTable();
		layout.setSize("100%", "100%");
		initWidget(layout);

		// left list part
		VerticalPanel leftListPanel = new VerticalPanel();
		layout.setWidget(0, 0, leftListPanel);
		layout.getCellFormatter().setWidth(0, 0, "18%");
		layout.getCellFormatter().setHeight(0, 0, "100%");
		leftListPanel.setWidth("100%");
		constructLeftListPart(leftListPanel);
		layout.getFlexCellFormatter().setAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);

		// middle part
		middlePanel = new SimplePanel();
		layout.setWidget(0, 1, middlePanel);
		layout.getCellFormatter().setWidth(0, 1, "52%");
		layout.getCellFormatter().setHeight(0, 1, "100%");
		middlePanel.setWidth("100%");
		initiateMiddlePanel();
		layout.getFlexCellFormatter().setAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);

		/**
		 * right detail part
		 */
		rightPanel = new SimplePanel();
		layout.setWidget(0, 2, rightPanel);
		layout.getCellFormatter().setWidth(0, 2, "30%");
		layout.getCellFormatter().setHeight(0, 2, "100%");
		rightPanel.setWidth("100%");
		layout.getFlexCellFormatter().setAlignment(0, 2,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);
	}

	private void constructLeftListPart(VerticalPanel panel) {
		// list title and updateMatchingStatus button
		HorizontalPanel title_regular = new HorizontalPanel();
		title_regular.addStyleName("TO_ONTOLOGY_title");
		panel.add(title_regular);
		title_regular.add(new Label("Term (Category) Pairs"));

		updateMatchingStatusBtn = new Image("images/refresh.jpg");
		updateMatchingStatusBtn
				.setTitle("Update ontology matches and ontology submissions of terms in this upload. ");
		updateMatchingStatusBtn.setHeight("20px");
		updateMatchingStatusBtn.addStyleName("right-align img_btn");
		title_regular.add(updateMatchingStatusBtn);

		// structure disclosure panel
		regularStrucureDisclosure = new DisclosurePanel("Structures");
		regularStrucureDisclosure.setWidth("100%");
		regularStrucureDisclosure.setAnimationEnabled(true);
		regularStructureList = new VerticalPanel();
		regularStructureList.setWidth("100%");
		regularStrucureDisclosure.setContent(regularStructureList);
		panel.add(regularStrucureDisclosure);

		// character disclosure panel
		regularCharacterDisclosure = new DisclosurePanel("Characters");
		regularCharacterDisclosure.setWidth("100%");
		regularCharacterDisclosure.setAnimationEnabled(true);
		regularCharacterList = new VerticalPanel();
		regularCharacterList.setWidth("100%");
		regularCharacterDisclosure.setContent(regularCharacterList);
		panel.add(regularCharacterDisclosure);

		// removed terms
		HorizontalPanel title_removed = new HorizontalPanel();
		title_removed.addStyleName("TO_ONTOLOGY_title");
		panel.add(title_removed);
		title_removed.add(new Label("Removed Pairs"));

		// removed structure list
		removedStructureDisclosure = new DisclosurePanel("Removed Structures");
		removedStructureDisclosure.setWidth("100%");
		removedStructureDisclosure.setAnimationEnabled(true);
		removedStructureList = new VerticalPanel();
		removedStructureList.setWidth("100%");
		removedStructureDisclosure.setContent(removedStructureList);
		panel.add(removedStructureDisclosure);

		// removed character list
		removedCharacterDisclosure = new DisclosurePanel("Removed Characters");
		removedCharacterDisclosure.setWidth("100%");
		removedCharacterDisclosure.setAnimationEnabled(true);
		removedCharacterList = new VerticalPanel();
		removedCharacterList.setWidth("100%");
		removedCharacterDisclosure.setContent(removedCharacterList);
		panel.add(removedCharacterDisclosure);
	}

	@Override
	public Image getRefreshBtn() {
		return this.updateMatchingStatusBtn;
	}

	public Widget asWidget() {
		return this;
	}

	/**
	 * use 1,2,3,4 to specify the 4 disclosure panels
	 */
	@Override
	public void updateTermCategoryPairsCount(ListType type, int count) {
		switch (type) {
		case REGULAR_STRUCTURE:
			this.regularStructureCount = count;
			this.regularStrucureDisclosure.getHeaderTextAccessor().setText(
					"Structures (" + Integer.toString(count) + ")");
			break;
		case REGULAR_CHARACTER:
			this.regularCharacterCount = count;
			this.regularCharacterDisclosure.getHeaderTextAccessor().setText(
					"Characters (" + Integer.toString(count) + ")");
			break;
		case REMOVED_STRUCTURE:
			this.removedStructureCount = count;
			this.removedStructureDisclosure.getHeaderTextAccessor().setText(
					"Removed Structures (" + Integer.toString(count) + ")");
			break;
		case REMOVED_CHARACTER:
			this.removedCharacterCount = count;
			this.removedCharacterDisclosure.getHeaderTextAccessor().setText(
					"Removed Characters (" + Integer.toString(count) + ")");
			break;
		default:
			break;
		}
	}

	@Override
	public VerticalPanel getListPanelByType(ListType type) {
		switch (type) {
		case REGULAR_STRUCTURE:
			return this.regularStructureList;
		case REGULAR_CHARACTER:
			return this.regularCharacterList;
		case REMOVED_STRUCTURE:
			return this.removedStructureList;
		case REMOVED_CHARACTER:
			return this.removedCharacterList;
		default:
			return null;
		}
	}

	@Override
	public VerticalPanel getRegularStructureList() {
		return this.regularStructureList;
	}

	@Override
	public VerticalPanel getRegularCharacterList() {
		return this.regularCharacterList;
	}

	@Override
	public VerticalPanel getRemovedStructureList() {
		return this.removedStructureList;
	}

	@Override
	public VerticalPanel getRemovedCharacterList() {
		return this.removedCharacterList;
	}

	@Override
	public int getListCountByType(ListType type) {
		switch (type) {
		case REGULAR_STRUCTURE:
			return this.regularStructureCount;
		case REGULAR_CHARACTER:
			return this.regularCharacterCount;
		case REMOVED_STRUCTURE:
			return this.removedStructureCount;
		case REMOVED_CHARACTER:
			return this.removedCharacterCount;
		default:
			return 0;
		}
	}

	@Override
	public SimplePanel getMiddlePanel() {
		return middlePanel;
	}

	@Override
	public SimplePanel getRightPanel() {
		return rightPanel;
	}

	@Override
	public void initiateMiddlePanel() {
		Label label = new Label("Select a term (category) pair from left. ");
		middlePanel.setWidget(label);
	}

}
