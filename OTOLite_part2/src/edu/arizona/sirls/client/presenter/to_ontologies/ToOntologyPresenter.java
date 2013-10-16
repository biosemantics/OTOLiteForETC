package edu.arizona.sirls.client.presenter.to_ontologies;

import java.util.ArrayList;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.arizona.sirls.client.event.to_ontologies.AddNewSubmissionEvent;
import edu.arizona.sirls.client.event.to_ontologies.AddNewSubmissionEventHandler;
import edu.arizona.sirls.client.event.to_ontologies.BackToDetailViewEvent;
import edu.arizona.sirls.client.event.to_ontologies.BackToDetailViewEventHandler;
import edu.arizona.sirls.client.event.to_ontologies.ClearSelectionEvent;
import edu.arizona.sirls.client.event.to_ontologies.ClearSelectionEventHandler;
import edu.arizona.sirls.client.event.to_ontologies.DeleteSubmissionEvent;
import edu.arizona.sirls.client.event.to_ontologies.DeleteSubmissionEventHandler;
import edu.arizona.sirls.client.event.to_ontologies.EditSubmissionEvent;
import edu.arizona.sirls.client.event.to_ontologies.EditSubmissionEventHandler;
import edu.arizona.sirls.client.event.to_ontologies.MoveTermCategoryPairEvent;
import edu.arizona.sirls.client.event.to_ontologies.MoveTermCategoryPairEventHandler;
import edu.arizona.sirls.client.event.to_ontologies.OntologyRecordClickEvent;
import edu.arizona.sirls.client.event.to_ontologies.OntologyRecordClickEventHandler;
import edu.arizona.sirls.client.event.to_ontologies.OntologyRecordSelectChangedEvent;
import edu.arizona.sirls.client.event.to_ontologies.OntologyRecordSelectChangedEventHandler;
import edu.arizona.sirls.client.event.to_ontologies.SubmitSubmissionEvent;
import edu.arizona.sirls.client.event.to_ontologies.SubmitSubmissionEventHandler;
import edu.arizona.sirls.client.event.to_ontologies.TermCategoryPairSelectedEvent;
import edu.arizona.sirls.client.event.to_ontologies.TermCategoryPairSelectedEventHandler;
import edu.arizona.sirls.client.presenter.MainPresenter;
import edu.arizona.sirls.client.presenter.Presenter;
import edu.arizona.sirls.client.rpc.ToOntologiesService;
import edu.arizona.sirls.client.rpc.ToOntologiesServiceAsync;
import edu.arizona.sirls.client.view.to_ontologies.EditSubmissionView;
import edu.arizona.sirls.client.view.to_ontologies.ListType;
import edu.arizona.sirls.client.view.to_ontologies.MatchDetailView;
import edu.arizona.sirls.client.view.to_ontologies.MatchSubmissionView;
import edu.arizona.sirls.client.view.to_ontologies.OperationType;
import edu.arizona.sirls.client.view.to_ontologies.SubmissionDetailView;
import edu.arizona.sirls.client.view.to_ontologies.TermCategoryPairView;
import edu.arizona.sirls.shared.beans.to_ontologies.MappingStatus;
import edu.arizona.sirls.shared.beans.to_ontologies.OntologyMatch;
import edu.arizona.sirls.shared.beans.to_ontologies.OntologyRecord;
import edu.arizona.sirls.shared.beans.to_ontologies.OntologyRecordType;
import edu.arizona.sirls.shared.beans.to_ontologies.OntologySubmission;
import edu.arizona.sirls.shared.beans.to_ontologies.TermCategoryLists;
import edu.arizona.sirls.shared.beans.to_ontologies.TermCategoryPair;

public class ToOntologyPresenter implements Presenter {
	public interface Display {
		/**
		 * left: list part
		 */
		Image getRefreshBtn();

		VerticalPanel getListPanelByType(ListType type);

		int getListCountByType(ListType type);

		VerticalPanel getRegularStructureList();

		VerticalPanel getRegularCharacterList();

		VerticalPanel getRemovedStructureList();

		VerticalPanel getRemovedCharacterList();

		void updateTermCategoryPairsCount(ListType type, int count);

		/**
		 * middle matches and submission part
		 */
		SimplePanel getMiddlePanel();

		/**
		 * right: detail part
		 */
		SimplePanel getRightPanel();

		Widget asWidget();
	}

	private final Display display;
	private ToOntologiesServiceAsync rpcService = GWT
			.create(ToOntologiesService.class);
	private HandlerManager eventBus = new HandlerManager(null);
	private String selectedTerm;
	private String selectedCategory;
	private Widget selectedPairLabel;
	private final HandlerManager globalEventBus;

	public ToOntologyPresenter(Display view, HandlerManager globalEventBus) {
		this.display = view;
		this.globalEventBus = globalEventBus;
	}

	@Override
	public void go(HasWidgets container) {
		bindEvents();
		container.clear();
		container.add(display.asWidget());
		fetchTermsList();
	}

	@Override
	public void bindEvents() {
		display.getRefreshBtn().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.alert("TODO: update matching status of entire list");
			}
		});

		eventBus.addHandler(TermCategoryPairSelectedEvent.TYPE,
				new TermCategoryPairSelectedEventHandler() {

					@Override
					public void onTermCategoryPairSelected(
							TermCategoryPairSelectedEvent event) {
						TermCategoryPair data = event.getData();
						selectedTerm = data.getTerm();
						selectedCategory = data.getCategory();
						selectedPairLabel = event.getWidget();
						updateMatchesAndSubmissions(selectedTerm,
								selectedCategory);
					}
				});

		eventBus.addHandler(MoveTermCategoryPairEvent.TYPE,
				new MoveTermCategoryPairEventHandler() {

					@Override
					public void onMove(MoveTermCategoryPairEvent event) {
						moveTermCategoryPair(event.getData(), event.getWidget());
					}
				});

		eventBus.addHandler(AddNewSubmissionEvent.TYPE,
				new AddNewSubmissionEventHandler() {

					@Override
					public void onClick(AddNewSubmissionEvent event) {
						dispayAddNewSubmissionView(selectedTerm,
								selectedCategory);
					}
				});

		eventBus.addHandler(OntologyRecordSelectChangedEvent.TYPE,
				new OntologyRecordSelectChangedEventHandler() {

					@Override
					public void onSelect(OntologyRecordSelectChangedEvent event) {
						displayOntologyRecordDetail(event.getSelectedRecord()
								.getType(), event.getSelectedRecord().getId());

					}
				});

		eventBus.addHandler(OntologyRecordClickEvent.TYPE,
				new OntologyRecordClickEventHandler() {

					@Override
					public void onClick(OntologyRecordClickEvent event) {
						updateSelectedOntologyRecord(event.getRecordID(),
								event.getType());
					}
				});

		eventBus.addHandler(EditSubmissionEvent.TYPE,
				new EditSubmissionEventHandler() {

					@Override
					public void onClick(EditSubmissionEvent event) {
						new EditSubmissionPresenter(new EditSubmissionView(
								event.getSubmission(),
								OperationType.UPDATE_SUBMISSION), eventBus)
								.go(display.getRightPanel());
					}
				});

		eventBus.addHandler(DeleteSubmissionEvent.TYPE,
				new DeleteSubmissionEventHandler() {

					@Override
					public void onClick(DeleteSubmissionEvent event) {
						deleteSubmission(event.getSubmission());
					}
				});

		eventBus.addHandler(BackToDetailViewEvent.TYPE,
				new BackToDetailViewEventHandler() {

					@Override
					public void onClick(BackToDetailViewEvent event) {
						// display the detail view
						displayOntologyRecordDetail(
								OntologyRecordType.SUBMISSION, event
										.getSubmission().getSubmissionID());
					}
				});

		eventBus.addHandler(SubmitSubmissionEvent.TYPE,
				new SubmitSubmissionEventHandler() {

					@Override
					public void onSubmit(SubmitSubmissionEvent event) {
						submitSubmission(event.getSubmission(),
								event.getSubmissionType());
					}
				});

		eventBus.addHandler(ClearSelectionEvent.TYPE,
				new ClearSelectionEventHandler() {

					@Override
					public void onClick(ClearSelectionEvent event) {
						clearSelection();
					}
				});

	}

	private void deleteSubmission(OntologySubmission submission) {
		rpcService.deleteSubmission(submission, MainPresenter.uploadID,
				new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						Window.alert("Submission Deleted. ");
						display.getRightPanel().clear();
						updateMatchesAndSubmissions(selectedTerm,
								selectedCategory);
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Server Error: failed in deleting a submission. \n\n"
								+ caught.getMessage());
					}
				});
	}

	/**
	 * submit a new or an update of submission to bioportal, update db
	 * accordingly
	 * 
	 * @param submission
	 * @param type
	 */
	private void submitSubmission(OntologySubmission submission,
			OperationType type) {
		rpcService.submitSubmission(submission, MainPresenter.uploadID, type,
				new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						updateMatchesAndSubmissions(selectedTerm,
								selectedCategory);
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Server Error: failed to submit submission to bioportal. \n\n"
								+ caught.getMessage());
					}
				});
	}

	private void clearSelection() {
		display.getRightPanel().clear();
		rpcService.clearSelection(
				Integer.toString(MainPresenter.uploadInfo.getGlossaryType()),
				selectedTerm, selectedCategory, new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						updateMatchesAndSubmissions(selectedTerm,
								selectedCategory);
						updateTermCategoryPairStatus(MappingStatus.NOT_MAPPED);

					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Server Error: failed in clear selection.\n\n"
								+ caught.getMessage());
					}
				});
	}

	private void dispayAddNewSubmissionView(String term, String category) {
		rpcService.getDefaultDataForNewSubmission(MainPresenter.uploadID, term,
				category, new AsyncCallback<OntologySubmission>() {

					@Override
					public void onSuccess(OntologySubmission result) {
						result.setSubmittedBy(MainPresenter.uploadInfo
								.getEtcUserName());
						new EditSubmissionPresenter(new EditSubmissionView(
								result, OperationType.NEW_SUBMISSION), eventBus)
								.go(display.getRightPanel());
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Server Error: failed in getting default values of a submission. \n\n"
								+ caught.getMessage());
					}
				});
	}

	private void updateTermCategoryPairStatus(MappingStatus status) {
		String styleName = "TO_ONTOLOGY_not_mapped";
		switch (status) {

		case MAPPED_TO_MATCH:
			styleName = "TO_ONTOLOGY_mapped_to_match";
			break;
		case MAPPED_TO_SUBMISSION:
			styleName = "TO_ONTOLOGY_mapped_to_submission";
			break;
		default:
			break;
		}
		selectedPairLabel.removeStyleName("TO_ONTOLOGY_not_mapped");
		selectedPairLabel.removeStyleName("TO_ONTOLOGY_mapped_to_match");
		selectedPairLabel.removeStyleName("TO_ONTOLOGY_mapped_to_submission");
		selectedPairLabel.addStyleName(styleName);
	}

	private void displayOntologyRecordDetail(OntologyRecordType type,
			String recordID) {
		display.getRightPanel().clear();
		if (type.equals(OntologyRecordType.MATCH)) {
			rpcService.getMatchDetail(recordID,
					new AsyncCallback<OntologyMatch>() {

						@Override
						public void onSuccess(OntologyMatch result) {
							new MatchDetailPresenter(
									new MatchDetailView(result)).go(display
									.getRightPanel());
						}

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Server Error: " + caught.getMessage());
						}
					});
		} else {
			rpcService.getSubmissionDetail(recordID,
					new AsyncCallback<OntologySubmission>() {

						@Override
						public void onSuccess(OntologySubmission result) {
							new SubmissionDetailPresenter(
									new SubmissionDetailView(result,
											MainPresenter.uploadInfo
													.isHasBioportalInfo()),
									eventBus).go(display.getRightPanel());
						}

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Server Error: " + caught.getMessage());
						}
					});
		}
	}

	private void updateSelectedOntologyRecord(String recordID,
			final OntologyRecordType type) {
		display.getRightPanel().clear();
		rpcService.updateSelectedOntologyRecord(MainPresenter.uploadID,
				selectedTerm, selectedCategory, recordID, type,
				new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						if (type.equals(OntologyRecordType.MATCH)) {
							updateTermCategoryPairStatus(MappingStatus.MAPPED_TO_MATCH);
						} else {
							updateTermCategoryPairStatus(MappingStatus.MAPPED_TO_SUBMISSION);
						}
						// Window.alert("Selection has been saved!");
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Server Error: error in updating selected ontology record. Please refresh the page and try again later. "
								+ caught.getMessage());
					}
				});
	}

	private void fetchTermsList() {
		rpcService.getTermCategoryLists(MainPresenter.uploadID,
				new AsyncCallback<TermCategoryLists>() {

					@Override
					public void onSuccess(TermCategoryLists result) {
						updateLists(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Server Error: " + caught.getMessage());

					}
				});
	}

	private void moveTermCategoryPair(TermCategoryPair tcPair,
			TermCategoryPairView widget) {
		final TermCategoryPair data = tcPair;
		final TermCategoryPairView w = widget;
		display.getMiddlePanel().clear();
		display.getRightPanel().clear();

		// update the server
		rpcService.moveTermCategoryPair(MainPresenter.uploadID,
				data.getPairID(), !data.isRemoved(), new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						ListType type = ListType.REGULAR_STRUCTURE, related_type = ListType.REMOVED_STRUCTURE;
						if (data.isStructure()) {
							if (data.isRemoved()) {
								type = ListType.REMOVED_STRUCTURE;
								related_type = ListType.REGULAR_STRUCTURE;
							} else {
								type = ListType.REGULAR_STRUCTURE;
								related_type = ListType.REMOVED_STRUCTURE;
							}
						} else {
							if (data.isRemoved()) {
								type = ListType.REMOVED_CHARACTER;
								related_type = ListType.REGULAR_CHARACTER;
							} else {
								type = ListType.REGULAR_CHARACTER;
								related_type = ListType.REMOVED_CHARACTER;
							}
						}
						/**
						 * need to update the lists
						 */
						// 1. remove from current list
						// display.asWidget().removeFromParent();
						w.removeFromParent();
						display.updateTermCategoryPairsCount(type,
								display.getListCountByType(type) - 1);

						// 2. add into the related list
						TermCategoryPair new_data = new TermCategoryPair(data
								.getPairID(), data.getTerm(), data
								.getCategory());
						new_data.setStatus(data.getStatus());
						new_data.setRemoved(!data.isRemoved());
						new_data.setIsStructure(data.isStructure());

						TermCategoryPairView item = new TermCategoryPairView(
								new_data);
						new TermCategoryPairPresenter(item, eventBus,
								globalEventBus).go(display
								.getListPanelByType(related_type));
						display.updateTermCategoryPairsCount(related_type,
								display.getListCountByType(related_type) + 1);
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Server Error: " + caught.getMessage());

					}
				});
	}

	/**
	 * update the middle part of the page
	 * 
	 * @param term
	 * @param category
	 */
	private void updateMatchesAndSubmissions(String term, String category) {
		final String selectedTerm = term;
		final String selectedCategory = category;
		display.getRightPanel().clear();
		rpcService.getOntologyRecords(MainPresenter.uploadID, term, category,
				new AsyncCallback<ArrayList<OntologyRecord>>() {

					@Override
					public void onSuccess(ArrayList<OntologyRecord> result) {
						new MatchSubmissionPresenter(new MatchSubmissionView(
								result, selectedTerm, selectedCategory,
								MainPresenter.uploadInfo.isHasBioportalInfo(),
								eventBus), eventBus, selectedTerm,
								selectedCategory).go(display.getMiddlePanel());
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Server Error: " + caught.getMessage());
					}
				});
	}

	/**
	 * update the left list view
	 * 
	 * @param lists
	 */
	private void updateLists(TermCategoryLists lists) {
		// structure
		ArrayList<TermCategoryPair> list = lists.getRegularStructures();
		display.updateTermCategoryPairsCount(ListType.REGULAR_STRUCTURE,
				list.size());
		for (TermCategoryPair structure : list) {
			new TermCategoryPairPresenter(new TermCategoryPairView(structure),
					eventBus, globalEventBus).go(display
					.getRegularStructureList());
		}

		// character
		list = lists.getRegularCharacters();
		display.updateTermCategoryPairsCount(ListType.REGULAR_CHARACTER,
				list.size());
		for (TermCategoryPair character : list) {
			new TermCategoryPairPresenter(new TermCategoryPairView(character),
					eventBus, globalEventBus).go(display
					.getRegularCharacterList());
		}

		// removed structure
		list = lists.getRemovedStructures();
		display.updateTermCategoryPairsCount(ListType.REMOVED_STRUCTURE,
				list.size());
		for (TermCategoryPair structure : list) {
			new TermCategoryPairPresenter(new TermCategoryPairView(structure),
					eventBus, globalEventBus).go(display
					.getRemovedStructureList());
		}

		// removed character
		list = lists.getRemovedCharacters();
		display.updateTermCategoryPairsCount(ListType.REMOVED_CHARACTER,
				list.size());
		for (TermCategoryPair character : list) {
			new TermCategoryPairPresenter(new TermCategoryPairView(character),
					eventBus, globalEventBus).go(display
					.getRemovedCharacterList());
		}
	}
}
