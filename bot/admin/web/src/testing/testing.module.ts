import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { NbEvaIconsModule } from '@nebular/eva-icons';
import { NbThemeModule } from '@nebular/theme';

import { FullscreenDirective } from '../app/shared/directives';

@NgModule({
  declarations: [FullscreenDirective],
  imports: [
    BrowserAnimationsModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forRoot([]),

    NbThemeModule.forRoot({ name: 'default' }),
    NbEvaIconsModule
  ],
  exports: [FullscreenDirective, CommonModule, FormsModule, ReactiveFormsModule]
})
export class TestingModule {}
