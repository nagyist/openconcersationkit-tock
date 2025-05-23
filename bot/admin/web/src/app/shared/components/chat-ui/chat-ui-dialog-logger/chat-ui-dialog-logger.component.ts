/*
 * Copyright (C) 2017/2025 SNCF Connect & Tech
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { Component, EventEmitter, Input, OnDestroy, Output } from '@angular/core';
import { ActionReport, Debug, DialogReport, Sentence, SentenceWithFootnotes } from '../../../model/dialog-data';
import { getDialogMessageUserAvatar, getDialogMessageUserQualifier } from '../../../utils';
import { NbDialogService } from '@nebular/theme';
import { TestDialogService } from '../../test-dialog/test-dialog.service';
import { Router } from '@angular/router';
import { StateService } from '../../../../core-nlp/state.service';
import { Subject, take } from 'rxjs';
import { NlpStatsDisplayComponent } from '../../../../test/dialog/nlp-stats-display/nlp-stats-display.component';
import { DebugViewerDialogComponent } from '../../debug-viewer-dialog/debug-viewer-dialog.component';
import { BotApplicationConfiguration } from '../../../../core/model/configuration';
import { BotConfigurationService } from '../../../../core/bot-configuration.service';
import { RagAnswerToFaqAnswerInfos } from '../../../../faq/faq-management/faq-management.component';
import { AnnotationComponent } from 'src/app/shared/components';

@Component({
  selector: 'tock-chat-ui-dialog-logger',

  templateUrl: './chat-ui-dialog-logger.component.html',
  styleUrl: './chat-ui-dialog-logger.component.scss'
})
export class ChatUiDialogLoggerComponent implements OnDestroy {
  private readonly destroy$: Subject<boolean> = new Subject();

  @Input() dialog: DialogReport;

  @Input() userMessageIsClickable?: boolean;
  @Input() botMessageIsClickable?: boolean;

  @Output() onMessageClicked = new EventEmitter();

  @Input() highlightedAction?: ActionReport;

  allConfigurations: BotApplicationConfiguration[];

  constructor(
    private testDialogService: TestDialogService,
    private nbDialogService: NbDialogService,
    private router: Router,
    public state: StateService,
    private botConfiguration: BotConfigurationService
  ) {}

  ngOnInit() {
    this.botConfiguration.configurations.pipe(take(1)).subscribe((conf) => {
      this.allConfigurations = conf;
    });
  }

  getUserName(action: ActionReport): string {
    return getDialogMessageUserQualifier(action.isBot());
  }

  getUserAvatar(action: ActionReport): string {
    return getDialogMessageUserAvatar(action.isBot());
  }

  dialogConnector() {
    if (!this.allConfigurations) return;
    const firstAction = this.dialog.actions.find((action) => action.applicationId);
    if (firstAction) {
      const applicationId = firstAction.applicationId;
      if (applicationId) {
        const configuration = this.allConfigurations.find((conf) => conf.applicationId === applicationId);
        if (configuration) {
          return configuration;
        }
      }
    }
  }

  getDialogConnectorLabel() {
    const configuration = this.dialogConnector();
    if (configuration) {
      return configuration.connectorType.label();
    }
  }

  getDialogConnectorIconUrl() {
    const configuration = this.dialogConnector();
    if (configuration) {
      return configuration.connectorType.iconUrl();
    }
  }

  getDialogConfigurationDetail() {
    const configuration = this.dialogConnector();
    if (configuration) {
      return `${configuration.name} > ${configuration.connectorType.label()} (${configuration.applicationId})`;
    }
  }

  normalizeLocaleCode(code: string): string {
    return StateService.normalizeLocaleCode(code);
  }

  nbUserQuestions(): number {
    return this.dialog.actions.filter((action) => !action.isBot()).length;
  }

  nbBotAnswers(): number {
    return this.dialog.actions.filter(
      (action) =>
        action.isBot() && !action.message?.isDebug() && ((action.message as Sentence).text || (action.message as Sentence).messages?.length)
    ).length;
  }

  nbRagAnswers(): number {
    return this.dialog.actions.filter((action) => action.isBot() && action.metadata?.isGenAiRagAnswer).length;
  }

  nbAnnotations(): number {
    return this.dialog.actions.filter((action) => !!action.annotation).length;
  }

  createFaq(action: ActionReport, actionsStack: ActionReport[]) {
    const actionIndex = actionsStack.findIndex((act) => act === action);
    if (actionIndex > 0) {
      let question;
      const questionAction = actionsStack[actionIndex - 1];

      if (questionAction.message.isDebug()) {
        const actionDebug = questionAction.message as unknown as Debug;
        question = actionDebug.data.condensed_question || actionDebug.data.user_question;
      } else if (!questionAction.isBot()) {
        const questionSentence = questionAction.message as unknown as Sentence;
        question = questionSentence.text;
      }

      const answerSentence = action.message as unknown as SentenceWithFootnotes;
      const answer: RagAnswerToFaqAnswerInfos = {
        text: answerSentence.text,
        applicationId: action.applicationId
      };

      if (answerSentence.footnotes) {
        answer.footnotes = answerSentence.footnotes;
      }

      if (question && answer) {
        this.router.navigate(['faq/management'], { state: { question, answer } });
      }
    }
  }

  testDialogSentence(action: ActionReport) {
    this.testDialogService.testSentenceDialog({
      sentenceText: (action.message as unknown as Sentence).text,
      applicationId: action.applicationId,
      sentenceLocale: action._nlpStats?.locale
    });
  }

  replayDialog() {
    this.testDialogService.replayDialog(this.dialog);
  }

  displayNlpStats(action: ActionReport) {
    if (action._nlpStats) {
      this.nbDialogService.open(NlpStatsDisplayComponent, {
        context: {
          data: {
            request: JSON.stringify(action._nlpStats.nlpQuery, null, 2),
            response: JSON.stringify(action._nlpStats.nlpResult, null, 2)
          }
        }
      });
    }
  }

  openObservabilityTrace(action: ActionReport) {
    window.open(action.metadata.observabilityInfo.traceUrl, '_blank');
  }

  containsAnnotation(action: ActionReport): boolean {
    return !!action.annotation;
  }

  openAnnotation(action: ActionReport) {
    this.nbDialogService.open(AnnotationComponent, {
      context: {
        dialogReport: this.dialog,
        actionReport: action
      }
    });
  }

  showDebug(action: ActionReport) {
    this.nbDialogService.open(DebugViewerDialogComponent, {
      context: {
        debug: (action.message as Debug).data
      }
    });
  }

  goToPlayground(action: ActionReport) {
    const debugData = (action.message as Debug).data;
    if (debugData.question_answering_prompt) {
      this.router.navigate(['playground'], { state: { question_answering_prompt: debugData.question_answering_prompt } });
    }
  }

  messageClicked(action: ActionReport): void {
    this.onMessageClicked.emit(action);
  }

  ngOnDestroy(): void {
    this.destroy$.next(true);
    this.destroy$.complete();
  }
}
