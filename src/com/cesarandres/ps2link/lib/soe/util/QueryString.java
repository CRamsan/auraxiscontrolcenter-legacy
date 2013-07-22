package com.cesarandres.ps2link.lib.soe.util;

import java.util.ArrayList;

/**
 * Created by cesar on 6/16/13.
 * 
 * This class will handle the creation of queries.
 */
public class QueryString {
	public static enum SearchModifier {
		EQUALS("="), LESSTHAN("=<"), GREATERTHAN("=>"), LESSEQUALTHAN("=["), GREATEREQUALTHAN("=]"), STARTSWITH("=^"), NOTCONTAIN("=!");

		private final String mod;

		private SearchModifier(final String modifier) {
			this.mod = modifier;
		}

		@Override
		public String toString() {
			return this.mod;
		}
	}

	public static enum QueryCommand {
		SHOW("c:show"),
		HIDE("c:hide"),
		SORT("c:sort"),
		HAS("c:has"),
		RESOLVE("c:resolve"),
		CASE("c:case"),
		LIMIT("c:limit"),
		LIMITPERDB("c:limitPerDB"),
		START("c:start");

		private final String command;

		private QueryCommand(final String queryCommand) {
			this.command = queryCommand;
		}

		@Override
		public String toString() {
			return this.command;
		}
	}

	public static final String QUESTION_MARK = "?";
	public static final String EQUALS = "=";

	private ArrayList<SearchParameter> listtOfParameters;

	public QueryString() {
		this.listtOfParameters = new ArrayList<SearchParameter>(1);
	}

	public static QueryString generateQeuryString() {
		return new QueryString();
	}

	public QueryString AddComparison(String key, SearchModifier modifier, String value) {
		this.listtOfParameters.add(new Condition(key, modifier, value));
		return this;
	}

	public QueryString AddCommand(QueryCommand command, String value) {
		this.listtOfParameters.add(new Command(command, value));
		return this;
	}

	@Override
	public String toString() {
		if (this.listtOfParameters.size() == 0) {
			return "";
		} else {
			StringBuilder builder = new StringBuilder();
			for (SearchParameter param : listtOfParameters) {
				builder.append(param.toString() + "&");
			}
			return builder.toString();
		}
	}

	private abstract class SearchParameter {
		protected String value;
	}

	private class Condition extends SearchParameter {
		private String key;
		private SearchModifier modifier;

		public Condition(String key, SearchModifier modifier, String value) {
			this.key = key;
			this.value = value;
			this.modifier = modifier;
		}

		@Override
		public String toString() {
			return this.key + this.modifier.toString() + this.value;
		}
	}

	private class Command extends SearchParameter {
		private QueryCommand command;

		public Command(QueryCommand command, String value) {
			this.command = command;
			this.value = value;
		}

		@Override
		public String toString() {
			return this.command.toString() + EQUALS + this.value;
		}
	}
}
