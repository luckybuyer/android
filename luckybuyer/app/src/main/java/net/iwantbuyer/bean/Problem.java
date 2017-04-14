package net.iwantbuyer.bean;

import java.util.List;

/**
 * Created by yangshuyu on 2017/4/10.
 */
public class Problem {

    private List<ProblemBean> problem;

    public List<ProblemBean> getProblem() {
        return problem;
    }

    public void setProblem(List<ProblemBean> problem) {
        this.problem = problem;
    }

    public static class ProblemBean {
        /**
         * _resource : Faq
         * answer : 以及有关那些次数支持.
         * created_at : 2017-04-10T13:36:17.432781+08:00
         * id : 27
         * question : 一起运行质量而且很多他的.
         */

        private String _resource;
        private String answer;
        private String created_at;
        private int id;
        private String question;

        public String get_resource() {
            return _resource;
        }

        public void set_resource(String _resource) {
            this._resource = _resource;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }
    }
}
